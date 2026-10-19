package de.ixn075.smash;

import de.ixn075.smash.character.CharacterManager;
import de.ixn075.smash.character.CharacterPlayerManager;
import de.ixn075.smash.commands.MapSetupCommand;
import de.ixn075.smash.commands.StartCommand;
import de.ixn075.smash.commands.VoteCommand;
import de.ixn075.smash.gamestate.GameStateManager;
import de.ixn075.smash.listeners.*;
import de.ixn075.smash.listeners.custom.CharacterChangeListener;
import de.ixn075.smash.listeners.custom.GameStateChangeListener;
import de.ixn075.smash.map.loader.MapLoader;
import de.ixn075.smash.map.setup.MapSetup;
import de.ixn075.smash.scoreboard.ScoreboardPlayerManager;
import de.ixn075.smash.timer.GameTimer;
import de.ixn075.smash.voting.VoteManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Difficulty;
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SmashPlugin extends JavaPlugin {

    private static SmashPlugin plugin;
    private GameStateManager gameStateManager;
    private CharacterPlayerManager characterPlayerManager;
    private ScoreboardPlayerManager scoreboardPlayerManager;
    private HashMap<Player, MapSetup> setups;
    private CharacterManager characterManager;
    private VoteManager voteManager;
    private GameTimer gameTimer;
    private List<Listener> listeners;
    private File configFile;
    private YamlConfiguration fileConfiguration;

    public static SmashPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        if (plugin == null) {
            plugin = this;
        } else {
            getLogger().severe("Could not assign plugin instance, disable plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        double classVersion = NumberUtils.toDouble(System.getProperty("java.class.version"));
        if (classVersion < 65.0) {
            getLogger().warning("You are using an unsupported java version! (class version: " + classVersion + ")");
            getLogger().warning("Please update to at least Java 21! (class version: 65.0)");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.yml");
        if (this.getDataFolder().mkdirs()) {
            this.getLogger().info("Initializing configuration...");
        }
        try {
            if (this.configFile.createNewFile()) {
                this.getLogger().info("Configuration file initialized!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        try {
            saveDefaultValues();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setups = new HashMap<>();
        listeners = new ArrayList<>();

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
        listeners.add(new CharacterChangeListener());
        listeners.add(new GameStateChangeListener());

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        List<Command> commands = new ArrayList<>();
        commands.add(new MapSetupCommand("mapsetup", "Setup command to configure maps.", "/mapsetup <abort, finish, set, start>"));
        commands.add(new StartCommand("start", "Start command to skip wait time.", "/start"));
        commands.add(new VoteCommand("vote", "Vote command to change map vote.", "/vote <map>"));

        for (Command command : commands) {
            getServer().getCommandMap().register("smash", command);
        }

        for (World world : getServer().getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setThundering(false);
            world.setStorm(false);
            world.setGameRule(GameRules.IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRules.REDUCED_DEBUG_INFO, true);
            world.setGameRule(GameRules.SEND_COMMAND_FEEDBACK, true);
            world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
            world.setGameRule(GameRules.ENTITY_DROPS, false);
            world.setGameRule(GameRules.ADVANCE_TIME, false);
            world.setGameRule(GameRules.ADVANCE_WEATHER, false);
            world.setGameRule(GameRules.SPAWN_PATROLS, false);
            world.setGameRule(GameRules.SPAWN_WANDERING_TRADERS, false);
            world.setGameRule(GameRules.SPAWN_WARDENS, false);
            world.setGameRule(GameRules.SPAWN_MOBS, false);
            world.setGameRule(GameRules.MOB_DROPS, false);
            world.setGameRule(GameRules.MOB_GRIEFING, false);
            world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, false);
            world.setGameRule(GameRules.LOG_ADMIN_COMMANDS, false);
            world.setGameRule(GameRules.KEEP_INVENTORY, false);
            world.setGameRule(GameRules.COMMAND_BLOCK_OUTPUT, false);
            world.setGameRule(GameRules.SHOW_DEATH_MESSAGES, false);
            world.setGameRule(GameRules.UNIVERSAL_ANGER, false);
            world.setGameRule(GameRules.MAX_ENTITY_CRAMMING, 8);
        }

        gameStateManager = new GameStateManager();

        characterPlayerManager = new CharacterPlayerManager();

        scoreboardPlayerManager = new ScoreboardPlayerManager();

        voteManager = new VoteManager();

        characterManager = new CharacterManager();
        characterManager.init();

        gameTimer = new GameTimer();

        MapLoader.loadMaps();
    }

    public void saveDefaultValues() throws IOException {
        Map<String, Object> listOfStrings = new HashMap<>();
        listOfStrings.put("min-players-count", 2);
        listOfStrings.put("names.character-selection", "<gold>Characters</gold>");
        listOfStrings.put("names.maps-selection", "<gold>Maps</gold>");

        listOfStrings.put("strings.prefix", "<gold>Smash</gold><dark_gray> => </dark_gray>");

        listOfStrings.put("strings.join", "<green>$name joined the game.</green>");
        listOfStrings.put("strings.quit", "<red>$name left the game.</red>");
        listOfStrings.put("strings.players-missing", "<gold>There are still players missing to start the game.</gold>");
        listOfStrings.put("strings.scoreboard.title", "<gold>Smash</gold> <dark_gray>|</dark_gray> $timer");

        listOfStrings.put("maps", List.of());

        AtomicInteger added = new AtomicInteger(0);
        listOfStrings.forEach((key, value) -> {
            if (!this.getFileConfiguration().contains(key)) {
                this.getFileConfiguration().set(key, value);
                added.addAndGet(1);
            }
        });

        if (added.get() != 0)
            getLogger().info("Saved " + added.get() + " default values into configuration.");

        this.fileConfiguration.save(configFile);
    }

    @Override
    public void onDisable() {

        // Plugin disable

        if (this.gameTimer != null) {
            this.getLogger().info("Force stopping game timer.");
            this.gameTimer.forceStop();
        }

        if (this.setups != null) {
            int count = setups.size();
            if (count > 0) {
                this.getLogger().info("Force stopping all " + count + " running game setups.");
                this.setups.clear();
            }
        }

        if (this.listeners != null) {
            this.getLogger().info("Cleanup all remaining listeners.");
            this.listeners.clear();
        }
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public CharacterPlayerManager getPlayerManager() {
        return characterPlayerManager;
    }

    public ScoreboardPlayerManager getScoreboardManager() {
        return scoreboardPlayerManager;
    }

    public HashMap<Player, MapSetup> getSetups() {
        return setups;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public File getConfigFile() {
        return configFile;
    }

    public YamlConfiguration getFileConfiguration() {
        return fileConfiguration;
    }
}
