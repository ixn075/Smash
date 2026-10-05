package de.ixn075.smash.map.loader;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.PluginConfigFile;
import de.ixn075.smash.map.Map;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class MapLoader {

    private static final FileConfiguration config = SmashPlugin.getPlugin().getConfig();
    private static final HashMap<String, Map> loadedMaps = new HashMap<>();

    public static boolean containsMap(String name) {
        return config.contains("maps." + name);
    }

    private static void removeMap(String name) {
        loadedMaps.remove(name);
    }

    public static Map getMapOfName(String name) {
        return loadedMaps.get(name);
    }

    public static void loadMaps() {
        ConfigurationSection section = config.getConfigurationSection("maps");
        if (section == null) return;
        section.getKeys(false).forEach((key) -> {
            loadMap(section.getString(key));
        });
    }

    @Contract("_ -> new")
    public static Map loadMap(String mapName) {
        ConfigurationSection configMapsSection = config.getConfigurationSection("maps");
        if (configMapsSection == null) return null;
        for (String key : configMapsSection.getKeys(false)) {
            for (String value : configMapsSection.getStringList(key)) {

            }
        }

        SmashPlugin.getPlugin().getLogger().info(locations.length + " spawn locations loaded.");
        if (locations.length == 0) {
            throw new IllegalArgumentException("Locations for map '" + mapName + "' are empty.");
        }
        return new Map(mapName, locations);
    }

    /**
     *
     * Gets the current loaded maps in the HashMap
     *
     * @return the loaded maps from the HashMap
     */
    public static HashMap<String, Map> getLoadedMaps() {
        return loadedMaps;
    }

    public static void clearMaps() {
        loadedMaps.clear();
    }
}
