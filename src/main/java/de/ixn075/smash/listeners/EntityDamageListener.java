package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.gamestate.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageListener implements Listener {

    @EventHandler
    void on(@NotNull EntityDamageEvent e) {
        if (!e.getEntityType().equals(EntityType.ITEM)) {
            e.setCancelled(true);
            return;
        }
        if (SmashPlugin.getPlugin().getGameStateManager().getCurrentState() != GameState.INGAME) {
            e.setCancelled(true);
            return;
        }
        if (!e.getEntityType().equals(EntityType.PLAYER)) {
            e.setDamage(0D);
        }
    }
}
