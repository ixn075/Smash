package de.ixn075.smash.listeners.custom;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.item.Item;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.countdown.EndCountdown;
import de.ixn075.smash.events.GameStateChangeEvent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class GameStateChangeListener implements Listener {

    @EventHandler
    void on(@NotNull GameStateChangeEvent e) {
        switch (e.getGameState()) {
            case INGAME -> {
                SmashPlugin.getPlugin().getGameTimer().start();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.getInventory().clear();
                    player.sendTitlePart(TitlePart.TITLE, MiniMsg.plainString("Hooray!", GREEN));
                    player.sendTitlePart(TitlePart.SUBTITLE, MiniMsg.plainString("The game begins!", GRAY));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500)));

                    ItemStack stoneItem = new Item(Material.STONE, 3, MiniMsg.plainString("CHEST", RED)).build();
                    ItemStack bedrockItem = new Item(Material.BEDROCK, 3, MiniMsg.plainString("BEDROCK", RED)).build();
                    assert stoneItem != null;
                    assert bedrockItem != null;
                    // new ItemSpreading(player.getLocation(), 5, List.of(stoneItem, bedrockItem));

                    // TODO: Teleport players to random locations of voted map
                }
            }
            case END -> {
                SmashPlugin.getPlugin().getGameTimer().stop();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.getInventory().clear();
                    player.sendTitlePart(TitlePart.TITLE, MiniMsg.plainString("End of the game!", RED));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500)));
                }
                EndCountdown.start();
            }
        }
    }
}
