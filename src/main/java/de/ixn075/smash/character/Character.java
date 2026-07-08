package de.ixn075.smash.character;

import net.kyori.adventure.text.Component;

import java.util.List;

public class Character {

    private final int id;
    private Component name;
    private List<Ability> abilities;

    Character(int id, Component name, List<Ability> abilities) {
        this.id = id;
        this.name = name;
        this.abilities = abilities;
    }

    public int getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbility(List<Ability> abilities) {
        this.abilities = abilities;
    }
}
