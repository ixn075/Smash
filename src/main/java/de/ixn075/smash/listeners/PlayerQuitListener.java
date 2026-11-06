package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.map.MapSetup;
import de.ixn075.smash.api.util.PlayerUtil;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.countdown.LobbyCountdown;
import de.ixn075.smash.strings.Strings;
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

    private final int online = Bukkit.getOnlinePlayers().size() - 1;
    private final int minPlayers = MiniMsg.integer("min-players-count");

    @EventHandler
    void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();

        // Delete damage count from player
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey damageCount = new NamespacedKey(SmashPlugin.getPlugin(), "damageCount");
        NamespacedKey damage = new NamespacedKey(SmashPlugin.getPlugin(), "damage");
        if (pdc.has(damageCount)) {
            pdc.remove(damageCount);
        }
        if (pdc.has(damage)) {
            pdc.remove(damage);
        }

        switch (SmashPlugin.getPlugin().getGameStateManager().getCurrentState()) {
            case LOBBY -> {
                PlayerUtil.broadcast(MiniMsg.string("strings.quit")
                        .replaceText(builder -> builder.matchLiteral("$name")
                                .replacement(player.getName())));
                if (online < minPlayers) {
                    LobbyCountdown.forceStop();
                    Bukkit.broadcast(Strings.PREFIX.append(MiniMsg.plainString("The countdown was stopped, no players online to proceed.", RED)));
                }

                MapSetup mapSetup = SmashPlugin.getPlugin().getSetups().get(player);
                if (mapSetup == null) {
                    return;
                }
                mapSetup.abort();
            }
            case INGAME -> {
                if (online < minPlayers) {
                    Bukkit.broadcast(Strings.PREFIX.append(MiniMsg.plainString("The server will be turned off, not enough players online to proceed the game.", RED)));
                }
                stopServer();
            }
        }
        e.quitMessage(empty());
    }

    private void stopServer() {
        // Stop server if no players are online
        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        Bukkit.shutdown();
    }
}
