package de.ixn075.smash.events;

import de.ixn075.smash.api.gamestate.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when the game state changes.
 */
public class GameStateChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final GameState gameState;
    protected boolean cancelled;

    public GameStateChangeEvent(GameState state) {
        this.gameState = state;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return {@code true} if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel {@code true} if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
