package de.ixn075.smash.api.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtil {

    /**
     * Sends a message to every player online on the server.
     *
     * @param message The message to send as a {@link Component}.
     */
    public static void broadcast(Component message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Sends a actionbar to every player online on the server.
     *
     * @param message The message to send as a {@link Component}.
     */
    public static void actionbar(Component message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(message);
        }
    }
}
