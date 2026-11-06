package de.ixn075.smash.map;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.api.map.Map;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public abstract class MapLoader {

    private static final FileConfiguration config = SmashPlugin.getPlugin().getFileConfiguration();
    private static final List<Map> cachedMaps = new LinkedList<>();

    public static boolean containsMap(String name) {
        return config.contains("maps." + name);
    }

    public static @Nullable Map getMapOfName(String name) {
        for (Map map : cachedMaps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

    public static void loadMaps() {
        ConfigurationSection section = config.getConfigurationSection("maps");
        if (section == null) return;
        section.getKeys(false).forEach((mapName) -> {
            Map map = loadMap(section.getString(mapName));
            cachedMaps.add(map);
        });
    }

    @Contract("_ -> new")
    public static @Nullable Map loadMap(String mapName) {
        ConfigurationSection configMapsSection = config.getConfigurationSection("maps"); // Maps section in config
        if (configMapsSection == null) return null;

        Location[] locations = new Location[configMapsSection.getKeys(false).size()];

        for (String key : configMapsSection.getKeys(false)) { // All map names from maps section (e.g. maps.Desert; maps.Temple)
            for (String value : configMapsSection.getStringList(key + ".locations")) { //
                Location location = config.getLocation(value);
                locations = new Location[]{location};
            }
        }

        if (locations.length == 0) {
            System.err.println("Error while loading a map: " + mapName);
            throw new RuntimeException("Locations for map '" + mapName + "' are empty");
        }

        return new Map(mapName, 0);
    }

    /**
     *
     * Gets the current loaded maps in the HashMap
     *
     * @return the loaded maps from the HashMap
     */
    public static List<Map> getCachedMaps() {
        return cachedMaps;
    }

    public static void clearCachedMaps() {
        for (Map map : cachedMaps) {
            cachedMaps.remove(map);
        }
    }

    public boolean checkValidation(String mapName) {
        Object section = config.get("maps." + mapName);

        return section != null;
    }
}
