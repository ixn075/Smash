package de.ixn075.smash.commands;

import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.map.loader.MapLoader;
import de.ixn075.smash.strings.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class VoteCommand extends Command {

    public VoteCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage, List.of("v"));
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (args.length == 1) {
            return MapLoader.getLoadedMaps().keySet().stream().filter(s -> s.startsWith(args[0])).toList();
        }
        return List.of();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Strings.PREFIX.append(Strings.ONLY_PLAYERS));
            return false;
        }
        if (args.length == 1) {
            String lowerCaseArg = args[0].toLowerCase();
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You voted for '" + lowerCaseArg + "'.", GREEN)));
        } else {
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("/vote <map>", GREEN)));
        }
        return false;
    }
}
