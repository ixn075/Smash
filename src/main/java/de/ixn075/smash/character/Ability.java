package de.ixn075.smash.character;

public enum Ability {

    STAMP_ATTACK("Stamp Attack"),
    BOW_ATTACK("Bow Attack"),
    NO_KNOCKBACK("No knockback"),
    SPEED_EFFECT("Speed"),
    SPEED_BOOST("Speed II"),
    JUMP_BOOST("Jump Boost");

    private final String name;

    Ability(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
