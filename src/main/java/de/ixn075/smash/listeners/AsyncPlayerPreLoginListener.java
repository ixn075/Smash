package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    void on(AsyncPlayerPreLoginEvent e) {
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        if (online >= max) {
            switch (SmashPlugin.getPlugin().getGameStateManager().getCurrentState()) {
                case LOBBY ->
                        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, MiniMsg.plainString("The server is full!", RED));
                case INGAME -> {

                    // Allowing connection to server to spectate the other online players while they're playing.
                    // This also allows the connection to the server, even if the server has reacted it's max online players;
                    e.allow();

                    // Manipulating the online players count to trick the online players that nobody is watching the game right now.
                    Player player = Bukkit.getPlayer(e.getUniqueId());
                    if (player != null) {
                        Bukkit.getOnlinePlayers().remove(player); // Remove player from online players list
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
                default ->
                        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MiniMsg.plainString("The game already ended.", RED));
            }
        }
    }
}
