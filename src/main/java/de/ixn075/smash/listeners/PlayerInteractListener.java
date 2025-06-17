package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.builder.GameInventory;
import de.ixn075.smash.builder.Item;
import de.ixn075.smash.builder.Skull;
import de.ixn075.smash.character.Character;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.map.loader.MapLoader;
import de.ixn075.smash.strings.Strings;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class PlayerInteractListener implements Listener {

    @EventHandler
    void on(@NotNull PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
            return;
        }
        Player player = e.getPlayer();

        switch (e.getMaterial()) {
            case CHEST -> {
                List<ItemStack> items = new ArrayList<>(Collections.emptyList());
                for (Character character : Character.values()) {
                    items.add(new Skull(1, character.data().name()).build());
                }
                new GameInventory(Strings.CHARACTERS_SELECTION_NAME).edit(editor -> {
                    for (int i = 0; i < items.size(); i++) {
                        editor.set(i, items.get(i));
                    }
                }).accept(player::openInventory);
                e.setCancelled(true);
            }
            case MAP -> {
                if (SmashPlugin.getPlugin().getSmashConfig().checkMaps()) {
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("There are not maps configured.", NamedTextColor.RED)));
                    player.sendMessage(Strings.PREFIX.append(MiniMsg.plain("Create maps with /setup start.", NamedTextColor.YELLOW)));
                    e.setCancelled(true);
                    return;
                }
                new GameInventory(MiniMsg.plain("Maps", NamedTextColor.GOLD)).edit(editor -> {
                    for (String mapName : MapLoader.getLoadedMaps().keySet()) {
                        editor.add(new Item(Material.PAPER, 1, MiniMsg.plain(mapName, GREEN)).build());
                        e.setCancelled(true);
                    }
                }).accept(player::openInventory);
                e.setCancelled(true);
            }
            default -> e.setCancelled(true);
        }
    }
}
