package de.ixn075.smash.events;

import de.ixn075.smash.character.Character;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharacterChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Character before;
    private final Character after;

    public CharacterChangeEvent(Character before, Character after) {
        this.before = before;
        this.after = after;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public @Nullable Character getBefore() {
        return before;
    }

    public @Nullable Character getAfter() {
        return after;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
