package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.builder.Item;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.countdown.LobbyCountdown;
import de.ixn075.smash.gamestate.GameState;
import de.ixn075.smash.strings.Strings;
import de.ixn075.smash.util.PlayerUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class PlayerJoinListener implements Listener {

    @EventHandler
    void on(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);

        if (SmashPlugin.getPlugin().getGameStateManager().is(GameState.LOBBY)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);

            AttributeInstance attackSpeed = player.getAttribute(Attribute.ATTACK_SPEED);
            if (attackSpeed != null && attackSpeed.getValue() != 1024) {
                attackSpeed.setBaseValue(1024); // Remove 1.9+ cooldown
            }

            if (!player.getInventory().isEmpty()) player.getInventory().clear();

            new Item(Material.CHEST, 1, MiniMsg.plain("Characters", GOLD), List.of(empty(), MiniMsg.plain("Character selection", GRAY), empty())).build(characters -> player.getInventory().setItem(2, characters));
            new Item(Material.MAP, 1, MiniMsg.plain("Maps", GOLD), List.of(empty(), MiniMsg.plain("Map selection", GRAY), empty())).build(maps -> player.getInventory().setItem(6, maps));

            PersistentDataContainer pdc = player.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(SmashPlugin.getPlugin(), "damageCount");

            if (!pdc.has(key)) {
                pdc.set(key, PersistentDataType.INTEGER, 0);
            }

            int online = Bukkit.getOnlinePlayers().size();
            int minPlayers = SmashPlugin.getPlugin().getSmashConfig().getInt("config.min-players");

            PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.mini("strings.join-message").replaceText(builder -> builder.matchLiteral("$name").replacement(player.getName()))));
            // minPlayers > 1
            if (online >= minPlayers) {
                LobbyCountdown.start(); // Start countdown if minimum players are reached
            } else {
                PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.plain("There are still players missing to start the game.", RED)));
            }
        } else if (SmashPlugin.getPlugin().getGameStateManager().is(GameState.INGAME)) {
            player.setGameMode(GameMode.SPECTATOR);
            if (!player.getInventory().isEmpty()) player.getInventory().clear();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getGameMode().equals(GameMode.SPECTATOR)) {
                    onlinePlayer.sendMessage(MiniMsg.plain("[+] " + player.getName(), GREEN));
                }
            }
        }

        e.joinMessage(empty());
    }
}
