package de.ixn075.smash.config;

import de.ixn075.smash.SmashPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class MiniMsg {

    private static MiniMessage MINI_MESSAGE;
    private static FileConfiguration CONFIG;

    public static void init() {
        MINI_MESSAGE = MiniMessage.miniMessage();
        CONFIG = SmashPlugin.getPlugin().getFileConfiguration();
    }

    public static @NonNull Component string(String path) {
        String string = CONFIG.getString(path);
        return plainString(string);
    }

    public static int integer(String path) {
        return CONFIG.getInt(path);
    }

    public static <R extends String> @NotNull Component plainString(R message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static <R extends String> @NotNull Component plainString(R message, NamedTextColor color) {
        return plainString(message).color(NamedTextColor.nearestTo(color));
    }
}
