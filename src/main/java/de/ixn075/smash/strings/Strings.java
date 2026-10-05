package de.ixn075.smash.strings;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.config.PluginConfigFile;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Strings {

    public static FileConfiguration config = SmashPlugin.getPlugin().getConfig();

    /**
     * Config strings
     */
    public static Component CHARACTER_SELECTION = config.getRichMessage("names.character_selection");
    public static Component MAPS_SELECTION = config.getRichMessage("names.maps_selection");
    public static Component PREFIX = config.getRichMessage("strings.prefix");
    public static Component SCOREBOARD_TITLE = config.getRichMessage("strings.scoreboard.title");

    // Player messages (Default)
    public static Component PERMISSION_REQUIRED = MiniMsg.plain("You have no permission to do that.", RED);
    public static Component ONLY_PLAYERS = MiniMsg.plain("You have to be a player to do that.", RED);
    public static Component UNKNOWN_COMMAND = MiniMsg.plain("Unknown sub-command. ('$command')", RED);

    // Map-Setup strings (Default)
    public static Component NO_SETUP_STARTED = MiniMsg.plain("No setup started.", RED);
    public static Component SETUP_STARTED_SUCCESS = MiniMsg.plain("Setup ('$name') started.", GREEN);
    public static Component SETUP_ALREADY_RUNNING = MiniMsg.plain("Setup ('$name') is already running.", YELLOW);
    public static Component SETUP_MAP_NOT_AVAILABLE = MiniMsg.plain("Setup ('$name') is currently not available.", RED);
}
