package de.ixn075.smash;

import de.ixn075.smash.commands.ConfigCommand;
import de.ixn075.smash.commands.MapSetupCommand;
import de.ixn075.smash.commands.VoteCommand;
import de.ixn075.smash.config.PluginConfig;
import de.ixn075.smash.gamestate.GameStateManager;
import de.ixn075.smash.listeners.*;
import de.ixn075.smash.listeners.custom.GameStateChangeListener;
import de.ixn075.smash.map.loader.MapLoader;
import de.ixn075.smash.map.setup.MapSetup;
import de.ixn075.smash.player.PlayerManager;
import de.ixn075.smash.timer.GameTimer;
import de.ixn075.smash.voting.VoteManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmashPlugin extends JavaPlugin {

    private static SmashPlugin plugin;
    private GameStateManager gameStateManager;
    private PlayerManager playerManager;
    private HashMap<Player, MapSetup> setups;
    private PluginConfig pluginConfig;
    private VoteManager voteManager;
    private GameTimer gameTimer;

    public static SmashPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        if (plugin == null) {
            plugin = this;
        } else {
            getLogger().severe("Could not assign new/another plugin instance, disable plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        double classVersion = NumberUtils.toDouble(System.getProperty("java.class.version"));
        if (classVersion < 61.0) {
            getLogger().warning("You are using a unsupported Java Version! (class version: " + classVersion + ")");
            getLogger().warning("Please update to at least Java 17 (class version: 61).");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        gameStateManager = new GameStateManager();
        playerManager = new PlayerManager();
        setups = new HashMap<>();
    }

    @Override
    public void onEnable() {
        pluginConfig = new PluginConfig("smash.yml");
        if (!pluginConfig.exists()) {
            getLogger().severe("Could not find configuration file, disable plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            pluginConfig.load();
            MapLoader.clearMaps();
            MapLoader.loadMaps();
            if (pluginConfig.empty()) {
                getLogger().warning("Configuration file is empty, resetting to default values.");
                pluginConfig.defaultValues();
                pluginConfig.save(exception -> {
                    if (exception != null) {
                        getLogger().severe("Error while saving default values to config." + exception);
                    }
                });
            }
        }

        if (getSmashConfig().checkMaps()) {
            getLogger().warning("No maps found.");
        }

        // DEACTIVATE: DEBUG COMPLICATIONS
        /*if (getSmashConfig().getInt("min-players") < 2) {
            getLogger().warning("Minimum players cannot be lower than 2, deactivating plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/

        List<Listener> listeners = new ArrayList<>();

        listeners.add(new AsyncPlayerPreLoginListener());
        listeners.add(new BlockBreakListener());
        listeners.add(new BlockPlaceListener());
        listeners.add(new EntityDamageListener());
        listeners.add(new EntityPickupItemListener());
        listeners.add(new FoodLevelChangeListener());
        listeners.add(new InventoryClickListener());
        listeners.add(new PlayerDeathListener());
        listeners.add(new PlayerDropItemListener());
        listeners.add(new PlayerInteractListener());
        listeners.add(new PlayerItemHeldListener());
        listeners.add(new PlayerJoinListener());
        listeners.add(new PlayerQuitListener());

        // custom event
        listeners.add(new GameStateChangeListener());

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        List<Command> commands = new ArrayList<>();
        commands.add(new ConfigCommand("config",
                "Config command to manage config.", "/config <discard, reload, save>"));
        commands.add(new MapSetupCommand("mapsetup",
                "Setup command to configure maps.", "/mapsetup <abort, finish, set, start>"));
        commands.add(new VoteCommand("vote",
                "Vote command to change map vote.", "/vote <map>"));

        for (Command command : commands) {
            getServer().getCommandMap().register("smash", command);
        }

        for (World world : getServer().getWorlds()) {
            getLogger().config("Configuring world '" + world.getName() + "'.");
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setThundering(false);
            world.setStorm(false);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            world.setGameRule(GameRule.UNIVERSAL_ANGER, false);
            world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 8);
        }

        voteManager = new VoteManager();
        gameTimer = new GameTimer();
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public HashMap<Player, MapSetup> getSetups() {
        return setups;
    }

    public PluginConfig getSmashConfig() {
        if (pluginConfig == null)
            plugin.getLogger().info("Config not initialized.");
        return pluginConfig;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
