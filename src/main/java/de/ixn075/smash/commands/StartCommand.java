package de.ixn075.smash.commands;

import de.ixn075.smash.strings.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StartCommand extends Command {

    public StartCommand(String name, String description, String usage) {
        super(name, description, usage, List.of("s"));
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Strings.PREFIX.append(Strings.ONLY_PLAYERS));
            return false;
        }
        if (!player.hasPermission("smash.mapsetup")) {
            player.sendMessage(Strings.PREFIX.append(Strings.PERMISSION_REQUIRED));
            return false;
        }
        return false;
    }
}
