package de.ixn075.smash.map;

import de.ixn075.smash.SmashPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class Map {

    public String name;
    public Location[] locations;

    public Map(String name, Location[] locations) {
        this.name = name;
        this.locations = locations;
    }

    public boolean write() throws IOException { // Must be saved ingame which can be done with "/config save".
        if (locations == null || locations.length < 2) {
            return false;
        }
        FileConfiguration config = SmashPlugin.getPlugin().getFileConfiguration();
        config.set("maps." + name + ".spawnLocations", locations);
        config.save(SmashPlugin.getPlugin().getConfigFile());
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }
}
