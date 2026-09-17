package de.ixn075.smash.listeners.custom;

import de.ixn075.smash.character.Character;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.events.CharacterChangeEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CharacterChangeListener implements Listener {

    @EventHandler
    void on(@NotNull CharacterChangeEvent e) {
        if (e.getCharacterAfter() == null) {
            e.setCancelled(true);
            return;
        }

        Player p = e.getPlayer();
        Character before = e.getCharacterBefore();
        Character after = e.getCharacterAfter();

        if (before == null) {
            p.sendActionBar(MiniMsg.plain("Your character has been set to $name.", NamedTextColor.GREEN).
                    replaceText(builder ->
                            builder.matchLiteral("$name").replacement(after.getName())));
        } else {
            p.sendActionBar(MiniMsg.plain("Your character has been changed to $name.", NamedTextColor.GREEN).
                    replaceText(builder ->
                            builder.matchLiteral("$name").replacement(after.getName())));
        }
    }
}
