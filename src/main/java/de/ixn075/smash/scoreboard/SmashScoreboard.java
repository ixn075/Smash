package de.ixn075.smash.scoreboard;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

public class SmashScoreboard {

    private final Scoreboard scoreboard;
    private final de.ixn075.smash.scoreboard.ScoreboardManager sm = SmashPlugin.getPlugin().getScoreboardManager();

    public SmashScoreboard() {
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        this.scoreboard = sm.getNewScoreboard();
    }

    public void init() {
        if (scoreboard.getObjective("SmashBoard") == null) {
            this.scoreboard.registerNewObjective("SmashBoard", Criteria.DUMMY,
                    MiniMsg.plain("<gold>Smash</gold>", NamedTextColor.YELLOW), RenderType.INTEGER);

        }
    }

    public void show(@NotNull Player player) {
        player.setScoreboard(this.scoreboard);
    }
}
