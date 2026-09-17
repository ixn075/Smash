package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.gamestate.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerItemHeldListener implements Listener {

    @EventHandler
    void on(@NotNull PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (SmashPlugin.getPlugin().getGameStateManager().isGameState(GameState.INGAME)) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                e.setCancelled(false);
                return;
            }
            // Prevent player from moving to other slots than 0
            if (e.getPreviousSlot() == 0) {
                e.setCancelled(true);
            } else {
                player.getInventory().setHeldItemSlot(0);
            }
        }
    }
}
