package de.ixn075.smash.listeners.custom;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.builder.Item;
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
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.getInventory().clear();
                    player.sendTitlePart(TitlePart.TITLE, MiniMsg.plain("Hooray!", GREEN));
                    player.sendTitlePart(TitlePart.SUBTITLE, MiniMsg.plain("The game begins!", GRAY));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO, Duration.ofMillis(2500), Duration.ZERO));

                    ItemStack chestItem = new Item(Material.CHEST, 3, MiniMsg.plain("CHEST", RED)).build();
                    ItemStack bedrockItem = new Item(Material.BEDROCK, 3, MiniMsg.plain("BEDROCK", RED)).build();
                    assert chestItem != null;
                    assert bedrockItem != null;
                    // new ItemSpreading(player.getLocation(), 5, List.of(chestItem, bedrockItem));

                    // TODO: Teleport players to random locations of voted map
                }
                SmashPlugin.getPlugin().getGameTimer().start();
            }
            case END -> {
                SmashPlugin.getPlugin().getGameTimer().stop();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.getInventory().clear();
                    player.sendTitlePart(TitlePart.TITLE, MiniMsg.plain("End of the game!", RED));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO, Duration.ofMillis(2000), Duration.ZERO));
                }
                EndCountdown.start();
            }
        }
    }
}
