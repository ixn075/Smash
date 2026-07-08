package de.ixn075.smash.character;

import de.ixn075.smash.config.MiniMsg;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class CharacterManager {

    public Character MARIO = new Character(0, MiniMsg.plain("Mario", NamedTextColor.RED), List.of(Ability.JUMP_BOOST));
    public Character DONKEY_KONG = new Character(1, MiniMsg.plain("Donkey Kong", NamedTextColor.GOLD), List.of(Ability.NO_KNOCKBACK));
    public Character FLASH = new Character(2, MiniMsg.plain("Flash", NamedTextColor.RED), List.of(Ability.SPEED_BOOST));

    public List<Character> getCharacters() {
        return List.of(MARIO, DONKEY_KONG, FLASH);
    }

    public Component getName(int id) {
        return getCharacters().get(id).getName();
    }

    public List<Ability> getAbilities(int id) {
        return getCharacters().get(id).getAbilities();
    }

    public void set(int id, Component component) {
        getCharacters().get(id).setName(component);
    }

    public void set(int id, List<Ability> abilities) {
        getCharacters().get(id).setAbility(abilities);
    }
}
