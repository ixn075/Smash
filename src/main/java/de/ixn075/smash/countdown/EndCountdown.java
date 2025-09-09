package de.ixn075.smash.countdown;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.gamestate.GameState;
import de.ixn075.smash.strings.Strings;
import de.ixn075.smash.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class EndCountdown {

    private static int seconds;

    public static void start() {
        if (!SmashPlugin.getPlugin().getGameStateManager().is(GameState.END)) {
            throw new IllegalStateException("Ending-Countdown can only be started in END-State.");
        }
        seconds = 15;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmashPlugin.getPlugin(), () -> {
            switch (seconds) {
                case 15, 10, 5, 4, 3, 2 ->
                        PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.plain("The server restarts in " + seconds + " seconds.", YELLOW)));
                case 1 ->
                        PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.plain("The server restarts in " + seconds + " second.", YELLOW)));
                case 0 -> {
                    PlayerUtil.broadcast(Strings.PREFIX.append(MiniMsg.plain("Restarting...", YELLOW)));
                    Bukkit.shutdown();
                    return;
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setLevel(seconds);
                player.setExp(seconds / 15f);
            }
            seconds--;
        }, 0L, 20L);
    }
}
