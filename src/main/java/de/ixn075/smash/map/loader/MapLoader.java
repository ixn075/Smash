package de.ixn075.smash.map.loader;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.PluginConfig;
import de.ixn075.smash.map.Map;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MapLoader {

    private static final PluginConfig config = SmashPlugin.getPlugin().getSmashConfig();
    private static final HashMap<String, Map> loadedMaps = new HashMap<>();

    public static boolean contains(String name) {
        return config.contains("maps." + name);
    }

    private static void removeMap(String name) {
        loadedMaps.remove(name);
    }

    public static Map getMapOfName(String name) {
        return loadedMaps.get(name);
    }

    public static void loadMaps() {
        ConfigurationSection section = config.getSection("maps");
        if (section == null) {
            return;
        }
        for (String mapName : section.getKeys(false)) {
            Map map = loadMap(mapName);
            loadedMaps.put(mapName, map);
        }
    }

    @Contract("_ -> new")
    private static @NotNull Map loadMap(String mapName) {
        Location[] locations = config.getLocs("maps." + mapName + ".spawnLocations");
        if (locations.length == 0) {
            throw new IllegalArgumentException("Locations for map '" + mapName + "' are empty.");
        }
        return new Map(mapName, locations);
    }

    public static void clearMaps() {
        loadedMaps.forEach((name, map) -> removeMap(name));
    }

    public static HashMap<String, Map> getLoadedMaps() {
        return loadedMaps;
    }
}
