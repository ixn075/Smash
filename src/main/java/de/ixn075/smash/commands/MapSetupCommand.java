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
            sender.sendMessage(Strings.ONLY_PLAYERS);
            return false;
        }
        if (!player.hasPermission("smash.mapsetup")) {
            player.sendMessage(Strings.PERMISSION_REQUIRED);
            return false;
        }
        if (args.length == 1) {
            if (!SmashPlugin.getPlugin().getGameStateManager().isGameState(GameState.LOBBY)) {
                player.sendMessage(Strings.SETUP_MAP_NOT_AVAILABLE);
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "begin" -> {
                    player.sendMessage(Strings.PREFIX.append(
                            MiniMsg.plainString("This sub command is used to start the setup of a map.", GRAY))
                    );
                    player.sendMessage(Strings.PREFIX.append(
                            MiniMsg.plainString("- begin <map-name> <spawn locations>", GREEN))
                    );
                    return false;
                }
                case "cancel" -> {
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    if (mapSetup == null) {
                        player.sendMessage(Strings.NO_SETUP_STARTED);
                        return false;
                    }
                    player.sendMessage(Strings.PREFIX.append(
                            MiniMsg.plainString("Deleting setup '" + mapSetup.getName() + "'...", YELLOW))
                    );
                    mapSetup.remove();
                    player.sendMessage(Strings.PREFIX.append(
                            MiniMsg.plainString("You cancelled the setup.", RED))
                    );
                    return true;
                }
                case "complete" -> {
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    if (mapSetup == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }

                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Checking conditions...", YELLOW)));
                    if (mapSetup.countLocations() < mapSetup.getIndexSize()) {
                        player.sendMessage(Strings.PREFIX.append(
                                MiniMsg.plainString("Not enough spawn positions set! (" + mapSetup.countLocations() + " of " + mapSetup.getIndexSize() + ")", RED)));
                        return false;
                    }

                    Map map = mapSetup.finish();
                    if (map == null) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Error while caching map, cancelling.", RED)));
                        return false;
                    }
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Trying to write map...", YELLOW)));
                    try {
                        if (map.write()) {
                            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Finished writing '" + map.getName() + "'.", GREEN)));

                            return true;
                        } else {
                            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Map '" + map.getName() + "' not cached because of an error.", RED)));
                            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Take a look inside the console to find out, what's wrong.", RED)));
                            return false;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default ->
                        player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(
                                builder -> builder.matchLiteral("$command").replacement(args[0]))));
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
                    sender.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("'" + args[1] + "' is not a valid index number.", RED)));
                    return false;
                }
                int index = NumberUtils.toInt(args[1]);
                if (index < 0 || index >= mapSetup.getIndexSize()) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("There is a set limit of " + mapSetup.getIndexSize() + " positions.", RED)));
                    return false;
                }
                mapSetup.setSpawnLocation(index, player.getLocation());
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Spawn location ('" + index + "') set.", GREEN)));
            } else if (args[0].equalsIgnoreCase("start")) {
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("You forgot the amount of spawn locations after the name.", GRAY)));
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("- start <map-name> <spawn locations>", GREEN)));
                return false;
            } else {
                player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("begin")) {
                // mapsetup begin[0] name[1] spawnlocations[2]
                String mapName = args[1];
                if (MapLoader.containsMap(mapName)) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Map '" + mapName + "' already exists.", RED)));
                    return false;
                }

                if (SmashPlugin.getPlugin().getSetups().get(player) != null) {
                    player.sendMessage(Strings.SETUP_ALREADY_RUNNING.replaceText(
                            builder -> builder.matchLiteral("$name").replacement(mapName)));
                    return false;
                }

                if (!NumberUtils.isParsable(args[2])) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Index size '" + args[2] + "' is not a valid number.", RED)));
                    return false;
                }

                int indexSize = NumberUtils.toInt(args[2]);
                if (indexSize >= 33) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Index size '" + args[2] + "' cannot exceed 32.", RED)));
                    return false;
                }

                if (indexSize <= 0) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Index size '" + args[2] + "' cannot be lower than 1.", RED)));
                    return false;
                }

                MapSetup mapSetup = new MapSetup(player, mapName, indexSize);
                player.sendMessage(Strings.SETUP_STARTED_SUCCESS.replaceText(
                        builder -> builder.matchLiteral("$name").replacement(mapSetup.getName())));
                return true;
            } else {
                player.sendMessage(Strings.UNKNOWN_COMMAND.replaceText(
                        builder -> builder.matchLiteral("$command").replacement(args[0])));
                return false;
            }
        } else {
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Use following arguments:", GRAY)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("begin <map-name> <size of spawn locations>", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("cancel", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("complete", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("set <index (beginning with 0)>", GREEN)));
            return false;
        }
        return false;
    }
}
