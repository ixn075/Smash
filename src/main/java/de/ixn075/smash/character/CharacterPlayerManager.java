package de.ixn075.smash.character;

import de.ixn075.smash.events.CharacterChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CharacterPlayerManager {

    private final Map<Player, Character> characters;

    public CharacterPlayerManager() {
        this.characters = new HashMap<>();
    }

    public Character getCharacter(Player player) {
        return characters.get(player);
    }

    public void setCharacter(Player player, Character character) {
        Bukkit.getPluginManager().callEvent(new CharacterChangeEvent(player, characters.get(player), character));
        characters.replace(player, character);
    }

    public Map<Player, Character> getCharacters() {
        return characters;
    }
}
