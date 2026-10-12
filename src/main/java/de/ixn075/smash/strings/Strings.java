package de.ixn075.smash.strings;

import de.ixn075.smash.config.MiniMsg;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Strings {

    /**
     * Config strings
     */
    public static Component CHARACTER_SELECTION = MiniMsg.string("names.character_selection");
    public static Component MAPS_SELECTION = MiniMsg.string("names.maps_selection");
    public static Component PREFIX = MiniMsg.string("strings.prefix");
    public static Component SCOREBOARD_TITLE = MiniMsg.string("strings.scoreboard.title");
    // Player messages (Default)
    public static Component PERMISSION_REQUIRED = MiniMsg.plainString("You have no permission to do that.", RED);
    public static Component ONLY_PLAYERS = MiniMsg.plainString("You have to be a player to do that.", RED);
    public static Component UNKNOWN_COMMAND = MiniMsg.plainString("Unknown sub-command. ('$command')", RED);
    public static Component NO_SETUP_STARTED = MiniMsg.plainString("No setup started.", RED);
    public static Component SETUP_STARTED_SUCCESS = MiniMsg.plainString("Setup ('$name') started.", GREEN);
    public static Component SETUP_ALREADY_RUNNING = MiniMsg.plainString("Setup ('$name') is already running.", YELLOW);
    public static Component SETUP_MAP_NOT_AVAILABLE = MiniMsg.plainString("Setup ('$name') is currently not available.", RED);

    public static void init() {
        MiniMsg.init();
    }
}
