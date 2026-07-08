package de.ixn075.smash.map.setup;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.map.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MapSetup {

    private final Player player;
    private final String name;
    private final Location[] spawnLocations;
    private final int indexSize;
    private short currentIndexSize;

    public MapSetup(Player player, String name, int indexSize) {
        this.player = player;
        this.name = name;
        this.spawnLocations = new Location[indexSize];
        this.indexSize = indexSize;
        this.currentIndexSize = 0;
        SmashPlugin.getPlugin().getSetups().put(player, this);
    }

    public void delete() {
        SmashPlugin.getPlugin().getSetups().remove(player);
    }

    public Map finish() {
        delete();
        return new Map(name, spawnLocations);
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }

    public int getIndexSize() {
        return indexSize;
    }

    public void setSpawnLocation(int index, Location location) {
        this.spawnLocations[index] = location;

    }

    public int countLocations() {
        currentIndexSize = 0;
        for (Location location : this.spawnLocations) {
            if (location != null) {
                currentIndexSize++;
            }
        }
        return currentIndexSize;
    }
}
