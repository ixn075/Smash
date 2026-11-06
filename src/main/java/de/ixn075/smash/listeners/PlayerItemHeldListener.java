package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.gamestate.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerItemHeldListener implements Listener {

    @EventHandler
    void on(@NotNull PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (SmashPlugin.getPlugin().getGameStateManager().getCurrentState() != GameState.INGAME) {
            e.setCancelled(false);
            return;
        }

        // Prevent player from moving to other slots than 0
        if (e.getPreviousSlot() == 0) {
            player.getInventory().setHeldItemSlot(2);
            e.setCancelled(true);
            return;
        }

        if (e.getPreviousSlot() == 2) {
            player.getInventory().setHeldItemSlot(6);
            e.setCancelled(true);
            return;
        }

        if (e.getPreviousSlot() == 6) {
            player.getInventory().setHeldItemSlot(2);
            e.setCancelled(true);
        }
    }
}
