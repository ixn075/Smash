package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.character.Character;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.player.PlayerManager;
import de.ixn075.smash.strings.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    @EventHandler
    void on(@NotNull InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            e.setCancelled(true);
            return;
        }
        Component itemName = e.getCurrentItem().getItemMeta().displayName();
        if (itemName == null) {
            e.setCancelled(true);
            return;
        }
        if (!e.getClick().equals(ClickType.LEFT)) {
            return;
        }
        PlayerManager pm = SmashPlugin.getPlugin().getPlayerManager();
        if (itemName.equals(Character.MARIO.data().name())) {
            pm.set(player, Character.MARIO);
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("<green>Selected Mario!</green>")));
        } else if (itemName.equals(Character.DONKEY_KONG.data().name())) {
            pm.set(player, Character.DONKEY_KONG);
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("<green>Selected Donkey Kong!</green>")));
        } else if (itemName.equals(Character.FLASH.data().name())) {
            pm.set(player, Character.FLASH);
            player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("<green>Selected Flash!</green>")));
        }
        e.setCancelled(true);
    }
}
