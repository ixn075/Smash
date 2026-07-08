package de.ixn075.smash.player;

import de.ixn075.smash.character.Character;
import de.ixn075.smash.events.CharacterChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private final Map<Player, Character> characters;

    public PlayerManager() {
        this.characters = new HashMap<>();
    }

    public Character getCharacter(Player player) {
        return characters.get(player);
    }

    public void set(Player player, Character character) {
        Bukkit.getPluginManager().callEvent(new CharacterChangeEvent(characters.get(player), character));
        characters.replace(player, character);
    }

    public Map<Player, Character> getCharacters() {
        return characters;
    }
}
