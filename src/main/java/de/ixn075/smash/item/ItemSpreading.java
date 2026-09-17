package de.ixn075.smash.item;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.List;

public class ItemSpreading {

    private final SecureRandom random;
    private final BukkitTask task;

    public ItemSpreading(Location location, int radius, List<ItemStack> items) {
        this.random = new SecureRandom();

        task = Bukkit.getScheduler().runTaskTimer(SmashPlugin.getPlugin(), () -> {
            if (SmashPlugin.getPlugin().getGameStateManager().isGameState(GameState.INGAME)) {
                // Calculate random position within the radius
                double angle = random.nextDouble() * 2 * Math.PI; // random angle
                double distance = random.nextDouble() * radius; // random distance within the radius
                double offsetX = distance * StrictMath.cos(angle);
                double offsetZ = distance * StrictMath.sin(angle);

                Location spreadLoc = location.clone().add(offsetX, location.clone().toBlockLocation().toHighestLocation().getY(), offsetZ);

                // Choose a random item
                ItemStack item = items.get(random.nextInt(items.size()));

                // Drop the item at the given location
                World world = location.getWorld();
                if (world != null) {
                    world.dropItem(spreadLoc, item);
                }
            }
        }, 0, 20 * 30); // 30 seconds interval
    }

    public void stop() {
        if (!task.isCancelled()) {
            task.cancel();
        }
    }
}
