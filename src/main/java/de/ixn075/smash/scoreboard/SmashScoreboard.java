package de.ixn075.smash.scoreboard;

import de.ixn075.smash.strings.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SmashScoreboard {

    private final ScoreboardManager bsm = Bukkit.getScoreboardManager();
    private final Scoreboard scoreboard = bsm.getMainScoreboard();
    private final Player player;

    private Objective objective;
    private Team team;

    public SmashScoreboard(Player player) {
        this.player = player;
        this.objective = scoreboard.getObjective("SmashBoard");
        if (this.objective == null) {
            objective = this.scoreboard.registerNewObjective("SmashBoard", Criteria.DUMMY,
                    Strings.SCOREBOARD_TITLE, RenderType.INTEGER);
        }
        this.team = scoreboard.getTeam("SmashBoard-Team");
        if (this.team == null) {
            this.scoreboard.registerNewTeam("SmashBoard-Team");
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setAutoUpdateDisplay(true);
        player.setScoreboard(this.scoreboard);
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
