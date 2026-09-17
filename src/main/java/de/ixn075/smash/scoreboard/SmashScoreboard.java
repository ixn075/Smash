package de.ixn075.smash.scoreboard;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.gamestate.GameState;
import de.ixn075.smash.strings.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

public class SmashScoreboard {

    private final ScoreboardManager bsm = Bukkit.getScoreboardManager();
    private final Scoreboard scoreboard = bsm.getMainScoreboard();
    private final ScoreboardPlayerManager spm = SmashPlugin.getPlugin().getScoreboardManager();
    private final Player player;
    private final GameState gameState;

    private Objective o = scoreboard.getObjective("SmashBoard");

    public SmashScoreboard(Player player, GameState gameState) {
        this.player = player;
        this.gameState = gameState;
    }

    public void create() {
        if (o == null) {
            o = this.scoreboard.registerNewObjective("SmashBoard", Criteria.TRIGGER,
                    Strings.SCOREBOARD_TITLE, RenderType.INTEGER);
            ;
        } else {
            o.setAutoUpdateDisplay(true);
        }
    }

    public void show() {
        try {
            player.setScoreboard(this.scoreboard);
            spm.add(player);
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
            e.printStackTrace(System.err);
        }
    }

    public void clear(@NotNull Player player) {
        spm.remove(player);
    }

    public Player getPlayer() {
        return player;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
