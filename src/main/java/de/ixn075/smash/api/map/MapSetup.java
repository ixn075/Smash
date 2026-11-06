package de.ixn075.smash.api.map;

import de.ixn075.smash.SmashPlugin;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to create a setup session with the given player online on the server.
 * If the player leaves the server while the session is still running, the setup will be terminated and deleted.
 *
 * @since 1.0
 */
public class MapSetup {

    private final UUID playerUuid;
    private final String mapName;
    private final List<Location> locations;

    public MapSetup(UUID playerUuid, String mapName) {
        this.playerUuid = playerUuid;
        this.mapName = mapName;
        this.locations = new ArrayList<>();
        SmashPlugin.getPlugin().getSetups().put(playerUuid, this);
    }

    public void abort() {
        SmashPlugin.getPlugin().getSetups().remove(playerUuid);
    }

    public Map finish() {
        if (locations.isEmpty()) {
            return null;
        }
        abort();
        return new Map(mapName, 0);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getMapName() {
        return mapName;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addLocation() {
        for (Location location : locations) {
            if (location != null) {
                locations.add(location);
            }
        }
    }

    public int locationSize() {
        return locations.size();
    }
}
