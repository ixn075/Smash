package de.ixn075.smash.config;

import de.ixn075.smash.SmashPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class MiniMsg {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final FileConfiguration CONFIG = SmashPlugin.getPlugin().getConfig();

    public static @Nullable Component mini(String path) {
        return CONFIG.getRichMessage(path);
    }

    public static <R extends String> @NotNull Component plain(R message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static <R extends String> @NotNull Component plain(R message, NamedTextColor color) {
        return plain(message).color(color);
    }
}
