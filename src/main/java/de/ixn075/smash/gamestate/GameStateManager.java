package de.ixn075.smash.gamestate;

import de.ixn075.smash.events.GameStateChangeEvent;
import org.bukkit.Bukkit;

public class GameStateManager {

    private GameState currentState;
    private boolean skip;

    public GameStateManager() {
        // ALWAYS starts with LOBBY gamestate
        this.currentState = GameState.LOBBY;
        this.skip = false;
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public boolean isSkip() {
        return skip;
    }

    public void set(GameState gamestate, boolean skip) {
        this.currentState = gamestate;
        this.skip = skip;
        // Call bukkit event when triggering method to set the gamestate
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(gamestate, skip));
    }

    public void skip() {
        if (isGameState(GameState.LOBBY)) {
            set(GameState.INGAME, true);
        } else if (isGameState(GameState.INGAME)) {
            set(GameState.END, true);
        }
    }

    public boolean isGameState(GameState state) {
        return this.currentState == state;
    }
}
