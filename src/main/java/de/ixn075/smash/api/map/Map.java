package de.ixn075.smash.api.map;

import de.ixn075.smash.SmashPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class
 */
public class Map {

    public String name;
    public int id;
    public List<Location> locations;

    public Map(String name, int id) {
        this.name = name;
        this.id = id;
        this.locations = new ArrayList<>();
    }

    /**
     *
     * This method is writing the map to the config.
     * The name, id and the locations must be set, otherwise it returns false and doesn't do anything.
     *
     * @return Whether the file was saved successfully (true) or else (false)
     * @throws IOException Throws exception when error occurs while saving the file.
     */
    public boolean write() throws IOException {
        if (locations == null || locations.size() < 2) {
            return false;
        }
        FileConfiguration config = SmashPlugin.getPlugin().getFileConfiguration();
        config.set("maps." + name + ".locations", locations);
        config.save(SmashPlugin.getPlugin().getConfigFile());
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
