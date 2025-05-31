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

    public ConfigCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage, List.of("cfg"));
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        if (sender.hasPermission("smash.config")) {
            if (args.length == 1) {
                return Stream.of("discard", "reload", "save").filter(s -> s.startsWith(args[0])).toList();
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
        if (args.length == 0) {
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- discard", GREEN)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- reload", GREEN)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- save", GREEN)));
            return false;
        } else if (args.length == 1) {
            PluginConfig config = SmashPlugin.getPlugin().getSmashConfig();
            switch (args[0].toLowerCase()) {
                case "discard" -> {
                    if (!config.isChanged()) {
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("No changes detected.", RED)));
                        return false;
                    }
                    config.discardChanges();
                    sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Changes discarded.", GREEN)));
                }
                case "reload" -> {
                    if (config.isChanged()) {
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("There are detected changes!", RED)));
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You can not reload without saving your changes.", RED)));
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use '/config save' to save the file.", RED)));
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("You can also discard the changes with '/config discard'.", RED)));
                        return false;
                    }
                    config.load();
                    sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Configuration reloaded.", GREEN)));
                }
                case "save" -> {
                    if (!config.isChanged()) {
                        sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("No changes detected.", RED)));
                        return false;
                    }
                    config.save(exception -> {
                        if (exception == null) {
                            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Configuration saved.", GREEN)));
                        }
                    });
                }
                default -> {
                    sender.sendMessage(Strings.PREFIX.append(Strings.UNKNOWN_COMMAND.replaceText(builder -> builder.matchLiteral("$command").replacement(args[0]))));
                    return false;
                }
            }
        } else {
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Use following arguments:", GRAY)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- discard", GREEN)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- reload", GREEN)));
            sender.sendMessage(Strings.PREFIX.append(MiniMsg.plain("- save", GREEN)));
            return false;
        }
        return false;
    }
}
