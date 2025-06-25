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
                return Stream.of("abort", "finish", "set", "start").filter(s -> s.startsWith(args[0])).toList();
            }
        }
        return List.of();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Strings.PREFIX.append(Strings.ONLY_PLAYERS));
            return false;
        }
        if (!player.hasPermission("smash.mapsetup")) {
            player.sendMessage(Strings.PREFIX.append(Strings.PERMISSION_REQUIRED));
            return false;
        }
        if (args.length == 1) {
            if (!SmashPlugin.getPlugin().getGameStateManager().is(GameState.LOBBY)) {
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("The setup is not possible while playing.", RED)));
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "abort" -> {
                    if (SmashPlugin.getPlugin().getSetups().get(player) == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    mapSetup.delete();
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You aborted the setup.", RED)));
                    return true;
                }
                case "finish" -> {
                    if (SmashPlugin.getPlugin().getSetups().get(player) == null) {
                        player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                        return false;
                    }
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    if (mapSetup.countLocations() < mapSetup.getIndexSize()) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Not enough spawn positions! (" + mapSetup.countLocations() + " of " + mapSetup.getIndexSize() + ")", RED)));
                        return true;
                    }
                    Map map = mapSetup.finish();
                    if (map == null) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Error while saving map.", GREEN)));
                        return false;
                    }
                    if (map.write()) {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map '" + map.name() + "' cached.", GREEN)));
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Save the map with '/config save'.", YELLOW)));
                        return true;
                    } else {
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map '" + map.name() + "' not cached because of an error.", RED)));
                        player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Look in the console to find out, what's wrong.", RED)));
                        return false;
                    }
                }
                case "set" -> {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("This sub command could be used to set the spawn locations of the map.", GRAY)));
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- set <index 0, 1, 2, ...>", GREEN)));
                    return false;
                }
                case "start" -> {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("This sub command could be used to start the setup of a map.", GRAY)));
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- start <map-name> <spawn locations>", GREEN)));
                    return false;
                }
                default ->
                        player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                // mapsetup set[0] [1]
                if (SmashPlugin.getPlugin().getSetups().get(player) == null) {
                    player.sendMessage(Strings.PREFIX.append(Strings.NO_SETUP_STARTED));
                    return false;
                }
                MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                if (!NumberUtils.isParsable(args[1])) {
                    sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Index '" + args[1] + "' is not a valid number.", RED)));
                    return false;
                }
                int index = NumberUtils.toInt(args[1]);
                if (index < 0) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Positions below 0 are not allowed.", RED)));
                    return false;
                }
                if (index >= mapSetup.getIndexSize()) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("There is a set limit of " + mapSetup.getIndexSize() + " positions.", RED)));
                    return false;
                }
                mapSetup.setSpawnLocation(index, player.getLocation());
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Spawn location ('" + index + "') set.", GREEN)));

            } else if (args[0].equalsIgnoreCase("start")) {
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You forget the amount of spawn locations after the name.", GRAY)));
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- start <map-name> <spawn locations>", GREEN)));
                return false;
            } else {
                player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("start")) {
                // mapsetup start[0] name[1] spawnlocations[2]
                String mapName = args[1];
                if (MapLoader.contains(mapName)) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Map '" + mapName + "' already exists.", RED)));
                    return false;
                }
                if (SmashPlugin.getPlugin().getSetups().get(player) != null) {
                    MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Setup '" + mapSetup.getName() + "' already running.", YELLOW)));
                    return false;
                }
                if (!NumberUtils.isParsable(args[2])) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Index size '" + args[2] + "' is not a valid number.", RED)));
                    return false;
                }
                int indexSize = NumberUtils.toInt(args[2]);
                MapSetup setup = new MapSetup(player, mapName, indexSize);
                player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Setup '" + setup.getName() + "' started.", GREEN)));
                return true;
            } else {
                player.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        } else {
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- abort", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- finish", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- set <index (beginning with 0)>", GREEN)));
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- start <map-name> <size of spawn locations>", GREEN)));
            return false;
        }
        return false;
    }
}
