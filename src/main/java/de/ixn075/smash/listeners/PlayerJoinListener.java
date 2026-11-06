package de.ixn075.smash.listeners;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.gamestate.GameState;
import de.ixn075.smash.api.util.PlayerUtil;
import de.ixn075.smash.api.item.Item;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.countdown.LobbyCountdown;
import de.ixn075.smash.scoreboard.SmashScoreboard;
import de.ixn075.smash.strings.Strings;
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

        GameState currentGameState = SmashPlugin.getPlugin().getGameStateManager().getCurrentState();
        if (currentGameState != GameState.LOBBY) {
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);

        // player internals

        AttributeInstance attackSpeed = player.getAttribute(Attribute.ATTACK_SPEED);
        if (attackSpeed != null && attackSpeed.getValue() != 1024) {
            attackSpeed.setBaseValue(1024); // Remove 1.9+ hit cooldown
        }

        if (!player.getInventory().isEmpty()) player.getInventory().clear();

        new Item(Material.CHEST, 1, Strings.CHARACTER_SELECTION, List.of(MiniMsg.plainString("-----------------", DARK_GRAY), MiniMsg.plainString("Character selection", GRAY), MiniMsg.plainString("-----------------", DARK_GRAY))).build(characters -> player.getInventory().setItem(2, characters));
        new Item(Material.MAP, 1, Strings.MAPS_SELECTION, List.of(MiniMsg.plainString("---------------", DARK_GRAY), MiniMsg.plainString("Map selection", GRAY), MiniMsg.plainString("---------------", DARK_GRAY))).build(maps -> player.getInventory().setItem(6, maps));

        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(SmashPlugin.getPlugin(), "damage");

        if (!pdc.has(key)) {
            pdc.set(key, PersistentDataType.INTEGER, 0);
        }

        int online = Bukkit.getOnlinePlayers().size();
        int minPlayers = MiniMsg.integer("min-players-count");

            /*
            TextDisplay display = player.getWorld().spawn(player.getLocation(), TextDisplay.class, entity -> {
                entity.text(Component.text(online + " players online!", NamedTextColor.WHITE));
                entity.setBillboard(Display.Billboard.VERTICAL); // pivot only around the vertical axis
                entity.setBackgroundColor(Color.BLACK); // make the background red
                entity.setBrightness(new Display.Brightness(15, 15));
            });
            display.remove();
            */

        SmashScoreboard scoreboard = new SmashScoreboard(player);
        // TODO Implement scoreboard teams and values

        PlayerUtil.actionbar(MiniMsg.string("strings.join").replaceText(
                builder -> builder.matchLiteral("$name")
                        .replacement(player.getName())));

        // minPlayers > 1
        if (online >= minPlayers) {
            LobbyCountdown.start(); // Start countdown if minimum players are reached
        } else {
            PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.string("strings.players-missing")));
        }

        if (SmashPlugin.getPlugin().getGameStateManager().getCurrentState() == GameState.INGAME) {
            player.setGameMode(GameMode.SPECTATOR);
            if (!player.getInventory().isEmpty()) player.getInventory().clear();
            player.sendActionBar(MiniMsg.plainString("You are now in spectator mode.", YELLOW));
        }
        e.joinMessage(empty());
    }
}
