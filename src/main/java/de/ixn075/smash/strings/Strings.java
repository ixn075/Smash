package de.ixn075.smash.strings;

import de.ixn075.smash.SmashPlugin;
import de.ixn075.smash.config.MiniMsg;
import de.ixn075.smash.config.PluginConfig;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class Strings {

    public static PluginConfig config = SmashPlugin.getPlugin().getSmashConfig();

    /**
     * Config strings
     */
    public static Component CHARACTER_SELECTION = config.getRichMsg("config.names.character_selection");
    public static Component MAPS_SELECTION = config.getRichMsg("config.names.maps_selection");
    public static Component PREFIX = config.getRichMsg("config.strings.prefix");
    public static Component SCOREBOARD_TITLE = config.getRichMsg("config.strings.scoreboard.title");


    public static Component PERMISSION_REQUIRED = MiniMsg.plain("You have no permission to do that.", RED);
    public static Component ONLY_PLAYERS = MiniMsg.plain("You have to be a player to do that.", RED);
    public static Component UNKNOWN_COMMAND = MiniMsg.plain("Unknown sub-command. ('$command')", RED);
    public static Component NO_SETUP_STARTED = MiniMsg.plain("No setup started.", RED);

    // Map-Setup strings (Default)
    public static Component SETUP_STARTED = MiniMsg.plain("Setup ('$name') started.", RED);
    public static Component SETUP_ALREADY_RUNNING = MiniMsg.plain("Setup already running.", RED);
    public static Component SETUP_MAP_ALREADY_EXISTS = MiniMsg.plain("Setup ('$name') started.", RED);
}
