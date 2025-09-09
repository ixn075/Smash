package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.countdown.LobbyCountdown;
import de.ixn075.smash.gamestate.GameState;
import de.ixn075.smash.map.setup.MapSetup;
import de.ixn075.smash.strings.Strings;
import de.ixn075.smash.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class PlayerQuitListener implements Listener {

    @EventHandler
    void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();

        // Delete damage count from player
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(SmashPlugin.getPlugin(), "damageCount");
        if (pdc.has(key)) {
            pdc.remove(key); // Delete key from data container when existing
        }

        // Remove player's setup when available.
        MapSetup c = SmashPlugin.getPlugin().getSetups().get(player);
        if (c != null)
            c.delete();

        int online = Bukkit.getOnlinePlayers().size() - 1;
        int minPlayers = SmashPlugin.getPlugin().getSmashConfig().getInt("min-players");
        if (SmashPlugin.getPlugin().getGameStateManager().is(GameState.LOBBY)) {
            // lobby state
            PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.mini("strings.quit").replaceText(builder -> builder.matchLiteral("$name").replacement(player.getName()))));
            if (online < minPlayers) {
                LobbyCountdown.forceStop();
                Bukkit.broadcast(Strings.PREFIX.append(MiniMsg.plain("The countdown was stopped, not enough players online to proceed.", RED)));
            }
        } else if (SmashPlugin.getPlugin().getGameStateManager().is(GameState.INGAME)) {
            // in-game state
            stopServer();
        } else {
            // end state
            stopServer();
        }
        e.quitMessage(empty());
    }

    private void stopServer() {
        // Stop server if no players are online
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.shutdown();
        }
    }
}
