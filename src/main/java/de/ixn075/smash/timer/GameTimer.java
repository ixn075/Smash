package de.ixn075.smash.timer;

import de.ixn075.smash.SmashPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer {

    private final BukkitScheduler scheduler;
    private int seconds = 0;
    private BukkitTask task;

    public GameTimer() {
        this.scheduler = Bukkit.getScheduler();
    }

    public void start() {
        task = scheduler.runTaskTimer(SmashPlugin.getPlugin(), () -> seconds++, 0, 20);
    }

    public void stop() {
        task.cancel();
    }

    public void forceStop() {
        if (task == null) {
            return;
        }
        if (!task.isCancelled() || task.getTaskId() != -1) {
            task.cancel();
        }
    }

    public boolean isRunning() {
        if (task == null) {
            return false;
        }
        return !task.isCancelled();
    }

    public int getSeconds() {
        return seconds;
    }
}
