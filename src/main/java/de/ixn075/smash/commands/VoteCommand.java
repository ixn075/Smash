package de.ixn075.smash.commands;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.map.Map;
import de.ixn075.smash.map.MapLoader;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.strings.Strings;
import de.ixn075.smash.voting.Vote;
import de.ixn075.smash.voting.VoteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class VoteCommand extends Command {

    private final VoteManager vm = SmashPlugin.getPlugin().getVoteManager();

    public VoteCommand(String name, String description, String usage) {
        super(name, description, usage, List.of());
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (args.length == 1) {
            // Old line as the loaded maps list was a hashmap
            //return MapLoader.getLoadedMaps().keySet().stream().filter(s -> s.startsWith(args[0])).toList();
            return MapLoader.getCachedMaps().stream().map(Map::getName).collect(Collectors.toList());
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
        if (args.length == 1) {
            Map map = MapLoader.getMapOfName(args[0]);
            if (map == null) {
                player.sendActionBar(Strings.PREFIX.append(MiniMsg.plainString("Map '" + args[0] + "' doesn't exist.", RED)));
                return false;
            }

            Vote vote = vm.vote(player.getUniqueId(), map);
            player.sendActionBar(Strings.PREFIX.append(MiniMsg.plainString("You voted for '" + vote.getMap().getName() + "'.", GREEN)));
        } else {
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("Use following arguments:", GRAY)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plainString(" ")));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plainString("/vote <map>", GREEN)));
        }
        return false;
    }
}
