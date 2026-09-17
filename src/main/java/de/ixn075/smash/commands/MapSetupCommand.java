package de.ixn075.smash.commands;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.gamestate.GameState;
import de.ixn075.smash.map.Map;
import de.ixn075.smash.map.loader.MapLoader;
import de.ixn075.smash.map.setup.MapSetup;
import de.ixn075.smash.strings.Strings;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MapSetupCommand extends Command {

    public MapSetupCommand(String name, String description, String usage) {
        super(name, description, usage, List.of());
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (sender.hasPermission("smash.mapsetup")) {
            if (args.length == 1) {
                return Stream.of("begin", "cancel", "complete", "set").filter(a -> a.startsWith(args[0])).toList();
            }
        }
        return List.of();
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Strings.PREFIX.append(Strings.ONLY_PLAYERS));
            return false;
        }
        if (!player.hasPermission("smash.mapsetup")) {
            player.sendMessage(Strings.PREFIX.append(Strings.PERMISSION_REQUIRED));
            return false;
        }
        if (args.length == 1) {
            if (!SmashPlugin.getPlugin().getGameStateManager().isGameState(GameState.LOBBY)) {
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("The setup is not possible while a game is running.", RED)));
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "begin" -> {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("This sub command is used to start the setup of a map.", GRAY)));
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- begin <map-name> <spawn locations>", GREEN)));
                    return false;
                }
                case "cancel" -> {
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    if (mapSetup == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }
                    mapSetup.delete();
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You cancelled the setup.", RED)));
                    return true;
                }
                case "complete" -> {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Checking conditions...", YELLOW)));
                    if (SmashPlugin.getPlugin().getSetups().get(player) == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    if (mapSetup.countLocations() < mapSetup.getIndexSize()) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Not enough spawn positions set! (" + mapSetup.countLocations() + " of " + mapSetup.getIndexSize() + ")", RED)));
                        return true;
                    }
                    Map map = mapSetup.finish();
                    if (map == null) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Error while caching map, cancelling.", RED)));
                        return false;
                    }
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Trying to write map...", YELLOW)));
                    if (map.write()) {
                        try {
                            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Writing...", YELLOW)));
                            SmashPlugin.getPlugin().getSmashConfig().trySave();
                            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map ('" + map.name() + "') saved.", GREEN)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    } else {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map '" + map.name() + "' not cached because of an error.", RED)));
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Take a look inside the console to find out, what's wrong.", RED)));
                        return false;
                    }
                }
                case "set" -> {
                    if (SmashPlugin.getPlugin().getSetups().get(player) == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);

                }
                default ->
                        player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                // mapsetup set[0] [0 (map name)] [1 (amount of spawn locations)]
                MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                if (mapSetup == null) {
                    player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                    return false;
                }
                if (!NumberUtils.isParsable(args[1])) {
                    sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Index '" + args[1] + "' is not a valid number.", RED)));
                    return false;
                }
                int index = NumberUtils.toInt(args[1]);
                if (index < 1) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Positions below 1 are not allowed.", RED)));
                    return false;
                }
                if (index > 32) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Positions cannot exceed 32.", RED)));
                    return false;
                }
                if (index >= mapSetup.getIndexSize()) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("There is a set limit of " + mapSetup.getIndexSize() + " positions.", RED)));
                    return false;
                }
                mapSetup.setSpawnLocation(index, player.getLocation());
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Spawn location ('" + index + "') set.", GREEN)));

            } else if (args[0].equalsIgnoreCase("start")) {
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You forgot the amount of spawn locations after the name.", GRAY)));
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- start <map-name> <spawn locations>", GREEN)));
                return false;
            } else {
                player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("begin")) {
                // mapsetup begin[0] name[1] spawnlocations[2]
                String mapName = args[1];
                if (MapLoader.contains(mapName)) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map '" + mapName + "' already exists.", RED)));
                    return false;
                }
                if (SmashPlugin.getPlugin().getSetups().get(player) != null) {
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Setup ('" + mapSetup.getName() + "') already running.", YELLOW)));
                    return false;
                }
                if (!NumberUtils.isParsable(args[2])) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Index size '" + args[2] + "' is not a valid number.", RED)));
                    return false;
                }
                int indexSize = NumberUtils.toInt(args[2]);
                MapSetup setup = new MapSetup(player, mapName, indexSize);
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Beginning ('" + setup.getName() + "') map setup.", GREEN)));
                return true;
            } else {
                player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        } else {
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("begin <map-name> <size of spawn locations>", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("cancel", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("complete", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("set <index (beginning with 0)>", GREEN)));
            return false;
        }
        return false;
    }
}
