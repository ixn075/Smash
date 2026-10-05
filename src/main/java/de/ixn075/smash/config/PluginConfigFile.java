package de.ixn075.smash.config;

import de.ixn075.smash.SmashPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PluginConfigFile {

    private final Logger logger;
    private final FileConfiguration config;
    private final HashMap<String, String> listOfStrings;
    private File configFile;
    private boolean changed;
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new instance of a SmashConfig class. It can be used to create a new file
     * <p>
     * Use #init(String fileName) (without .yml) to create a new file.
     */
    public PluginConfigFile() {
        this.logger = SmashPlugin.getPlugin().getLogger();
        this.config = SmashPlugin.getPlugin().getConfig();
        this.changed = false;
        this.listOfStrings = new HashMap<>();
    }

    private void createRequiredFile(@Pattern("[a-z_\\-.]+") String fileName) throws IOException {
        this.configFile = new File("plugins/Smash/", fileName);
        if (configFile.getParentFile().mkdirs()) {
            this.logger.info(String.format("'%s' folder created.", configFile.getParentFile().getName()));
        }
        if (configFile.createNewFile()) {
            this.logger.info(String.format("'%s' file created.", fileName));
        }
    }

    public boolean init() throws IOException {
        createRequiredFile("smash.yml");
        load(false);
        return true;
    }

    public void checkForMissingConfigStrings() throws IOException {
        if (!fileConfiguration.getKeys(false).isEmpty()) {
            if (!this.changed) this.changed = true;
            for (String key : listOfStrings.keySet()) {
                set(key, listOfStrings.get(key));
            }
        }

        if (listOfStrings.isEmpty()) {
            return;
        }

        for (String key : listOfStrings.keySet()) {
            if (key == null) {
                return;
            }
            String value = listOfStrings.get(key);
            if (value == null) {
                return;
            }
            set(key, value);
        }

        trySave();
    }

    private void checkDefaultValues() throws IOException {
        set("config.min-players", 2); // Two players are enough for a game, I guess. :)

        set("config.names.character_selection", "<gold>Characters</gold>");
        set("config.names.maps_selection", "<gold>Maps</gold>");

        set("config.strings.prefix", "<gold>Smash</gold><dark_gray> => </dark_gray>");

        set("config.strings.join", "<green>$name joined the server.</green>");
        set("config.strings.quit", "<red>$name left the server.</red>");
        set("config.strings.players-missing", "<gold>There are still players missing to start the game.</gold>");
        set("config.strings.scoreboard.title", "<gold>Smash</gold> <dark_gray>|</dark_gray> $timer");

        set("config.maps", Collections.emptyList());

        trySave();
    }

    public Component getRichMsg(@NotNull String path) {
        return config.getRichMessage(path);
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int def) {
        return fileConfiguration.getInt(path, def);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return fileConfiguration.getBoolean(path, def);
    }

    public <V> void set(String path, V value) {
        fileConfiguration.set(path, value);
    }

    public Location[] getLocations(String path) {
        List<?> configList = getList(path);
        List<Location> locationList = new ArrayList<>();
        if (configList != null) {
            for (Object object : configList) {
                if (object instanceof Location location) {
                    locationList.add(location);
                }
            }
        }

        if (!locationList.isEmpty()) {

            Location[] locations = new Location[locationList.size()];

            int i = 0;
            for (Location location : locationList) {
                locations[i] = location;
                i++;
            }

            return locations;
        }
        return null;
    }

    public List<?> getList(String path) {
        return fileConfiguration.getList(path);
    }

    public ConfigurationSection getSection(String path) {
        return fileConfiguration.getConfigurationSection(path);
    }

    public boolean mapsEmpty() {
        if (isSection("maps"))
            return getSection("maps").getKeys(false).isEmpty();
        if (isList("maps"))
            return getList("maps").isEmpty();
        this.logger.warning(String.format("'%s' does not exist.", configFile.getParentFile().getName()));
        return false;
    }

    public boolean isChanged() {
        return changed;
    }

    public void trySave() throws IOException {
        this.fileConfiguration.save(configFile);
    }

    public void discardChanges() {
        this.changed = false;
    }

    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }

    public boolean isSection(String path) {
        return fileConfiguration.isConfigurationSection(path);
    }

    public boolean isList(String path) {
        return fileConfiguration.isList(path);
    }

    public boolean exists() {
        return configFile.exists();
    }

    public boolean isEmpty() {
        return fileConfiguration.getKeys(true).isEmpty();
    }

    public void load(boolean reload) {
        if (reload) {
            fileConfiguration = null;
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public HashMap<String, String> getListOfStrings() {
        return listOfStrings;
    }

    public File getConfigFile() {
        return configFile;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }
}
