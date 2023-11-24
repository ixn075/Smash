package de.clientdns.smash.config;

import de.clientdns.smash.SmashPlugin;
import de.clientdns.smash.map.loader.MapLoader;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SmashConfig {

    private final Logger logger;
    private final File configFile;
    private boolean changed;
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new instance of a SmashConfig class. It can be used to create a new file
     *
     * @param fileName The name to create a file under it.
     */
    public SmashConfig(@NotNull @Pattern("[a-z_\\-.]+") String fileName) {
        this.logger = SmashPlugin.getPlugin().getLogger();
        this.logger.info("Checking configuration.");
        this.configFile = new File("plugins/Smash/", fileName);
        this.changed = false;
        try {
            if (configFile.getParentFile().mkdirs()) {
                this.logger.info(String.format("'%s' created.", configFile.getParentFile().getName()));
            }
            if (configFile.createNewFile()) {
                this.logger.info(String.format("'%s' file created.", fileName));
            }
        } catch (Throwable e) {
            throw new RuntimeException("Error while creating file", e);
        }
    }

    public <V> void set(@NotNull String path, V value) {
        if (fileConfiguration.contains(path)) {
            return;
        }
        fileConfiguration.set(path, value);
        if (!changed) changed = true;
    }

    public void reset() {
        this.logger.info("Resetting configuration.");
        set("prefix", "<gold>Smash</gold> <dark_gray>|</dark_gray> ");
        set("min-players", 2);
        set("permission-required", "<red>You have no permission to do that.</red>");
        set("unknown-command", "<red>Unknown command. ($command)</red>");
        set("join-message", "<green>$name joined the server.</green>");
        set("quit-message", "<red>$name left the server.</red>");
        set("maps", List.of());
    }

    public String getStr(String path) {
        return getStr(path, null);
    }

    public String getStr(String path, String def) {
        return fileConfiguration.getString(path, def);
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int def) {
        return fileConfiguration.getInt(path, def);
    }

    public Location[] getLocs(String path) {
        List<?> configList = getList(path);
        List<Location> locs = new ArrayList<>();
        if (configList != null) {
            for (Object object : configList) {
                if (object instanceof Location location) {
                    locs.add(location);
                }
            }
        }

        if (!locs.isEmpty()) {

            Location[] locationArray = new Location[locs.size()];

            int i = 0;
            for (Location location : locs) {
                locationArray[i] = location;
                i++;
            }

            return locationArray;
        }
        return null;
    }

    public List<?> getList(String path) {
        return fileConfiguration.getList(path);
    }

    public ConfigurationSection getSection(String path) {
        return fileConfiguration.getConfigurationSection(path);
    }

    public boolean noMaps() {
        if (isSection("maps"))
            return getSection("maps").getKeys(false).isEmpty();
        if (isList("maps"))
            return getList("maps").isEmpty();
        return true;
    }

    public boolean isChanged() {
        return changed;
    }

    public void save(@NotNull Consumer<Throwable> consumer) {
        this.logger.info("Saving configuration.");
        try {
            if (!changed) {
                this.logger.info("No changes detected, cancelling.");
            } else {
                // There are unsaved changes left
                fileConfiguration.save(configFile);
                discardChanges();
            }
        } catch (IOException exception) {
            consumer.accept(exception);
        } finally {
            consumer.accept(null);
        }
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

    public boolean empty() {
        return fileConfiguration.getKeys(true).isEmpty();
    }

    public void load() {
        this.changed = false;
        this.logger.info("Loading configuration.");
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        MapLoader.clearMaps();
        MapLoader.loadMaps();
    }
}
