package de.ixn075.smash.scoreboard;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.strings.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

public class SmashScoreboard {

    private final Scoreboard scoreboard;
    private final ScoreboardPlayerManager sm = SmashPlugin.getPlugin().getScoreboardManager();
    private final Player player;

    public SmashScoreboard(Player player) {
        this.player = player;
        ScoreboardManager bsm = Bukkit.getScoreboardManager();
        this.scoreboard = bsm.getNewScoreboard();
        if (scoreboard.getObjective("SmashBoard") == null) {
            this.scoreboard.registerNewObjective("SmashBoard", Criteria.DUMMY,
                    Strings.SCOREBOARD_TITLE, RenderType.INTEGER);
            sm.add(player);
        }
    }

    public void show() throws IllegalArgumentException, IllegalStateException {
        if (!sm.getPlayers().contains(player)) {
            player.setScoreboard(this.scoreboard);
        }
    }

    public void clear(@NotNull Player player) throws IllegalArgumentException, IllegalStateException {
        if (sm.getPlayers().contains(player)) {
            player.setScoreboard(this.scoreboard);
        }
    }
}
