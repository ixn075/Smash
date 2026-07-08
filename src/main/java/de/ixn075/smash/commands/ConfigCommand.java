package de.ixn075.smash.commands;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.config.PluginConfig;
import de.ixn075.smash.strings.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class ConfigCommand extends Command {

    public ConfigCommand(String name, String description, String usage) {
        super(name, description, usage, List.of());
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (sender.hasPermission("smash.config")) {
            if (args.length == 1) {
                return Stream.of("reload").filter(s -> s.startsWith(args[0])).toList();
            }
        }
        return List.of();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (!sender.hasPermission("smash.config")) {
            sender.sendMessage(Strings.PREFIX.append(Strings.PERMISSION_REQUIRED));
            return false;
        }
        if (args.length != 1) {
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("reload", GREEN)));
            return false;
        } else {
            PluginConfig config = SmashPlugin.getPlugin().getSmashConfig();
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Reloading...", YELLOW)));
                config.reload();
                sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Configuration reloaded.", GREEN)));
            } else {
                sender.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                return false;
            }
        }
        return false;
    }
}
