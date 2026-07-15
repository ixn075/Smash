package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.character.CharacterManager;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.player.PlayerManager;
import de.ixn075.smash.strings.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    private CharacterManager cm;

    @EventHandler
    void on(@NotNull InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        InventoryView v = e.getView();

        if (!e.isLeftClick()) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) {
            e.setCancelled(true);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            e.setCancelled(true);
            return;
        }

        Component itemName = meta.itemName();

        if (v.title().contains(Strings.CHARACTER_SELECTION)) {
            Component m = MiniMsg.plain("Selected $name.", NamedTextColor.GREEN);
            PlayerManager pm = SmashPlugin.getPlugin().getPlayerManager();
            cm = SmashPlugin.getPlugin().getCharacterManager();
            if (itemName.equals(cm.MARIO.getName())) {
                pm.set(p, cm.MARIO);
                m = m.replaceText(builder ->
                        builder.matchLiteral("$name").replacement(cm.MARIO.getName()).once());
                p.sendActionBar(m);
            } else if (itemName.equals(cm.DONKEY_KONG.getName())) {
                pm.set(p, cm.DONKEY_KONG);
                m = m.replaceText(builder ->
                        builder.matchLiteral("$name").replacement(cm.DONKEY_KONG.getName()).once());
                p.sendActionBar(m);
            } else if (itemName.equals(cm.FLASH.getName())) {
                pm.set(p, cm.FLASH);
                m = m.replaceText(builder ->
                        builder.matchLiteral("$name").replacement(cm.FLASH.getName()).once());
                p.sendActionBar(m);
            }
        } else if (v.title().contains(Strings.MAPS_SELECTION)) {
            // Implement logic
            e.setCancelled(true);
            return;
        } else {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
    }
}
