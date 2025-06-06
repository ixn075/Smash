package de.ixn075.smash.map;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.PluginConfig;
import org.bukkit.Location;

public record Map(String name, Location[] spawnLocations) {

    public boolean write() { // Must be saved ingame which can be done with "/config save".
        if (spawnLocations == null || spawnLocations.length < 2) {
            return false;
        }
        PluginConfig config = SmashPlugin.getPlugin().getSmashConfig();
        if (config == null) {
            SmashPlugin.getPlugin().getLogger().info("Could not get config file, exiting.");
            return false;
        }
        config.setValue("maps." + name + ".spawnLocations", spawnLocations);
        return true;
    }
}
