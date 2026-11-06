package de.ixn075.smash.gamestate;

import de.ixn075.smash.api.gamestate.GameState;
import de.ixn075.smash.countdown.LobbyCountdown;
import de.ixn075.smash.events.GameStateChangeEvent;
import org.bukkit.Bukkit;

public class GameStateManager {

    private GameState currentState;

    public GameStateManager() {
        // ALWAYS starts with LOBBY gamestate
        this.currentState = GameState.LOBBY;
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public void set(GameState gamestate) {
        this.currentState = gamestate;
        // Call bukkit event when triggering method to set the gamestate
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(gamestate));
    }

    public void skipLobbyPhase() {
        if (currentState == GameState.LOBBY) {
            LobbyCountdown.forceStop();
            set(GameState.INGAME);
        }
    }
}
