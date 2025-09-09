package de.ixn075.smash.strings;

import de.ixn075.smash.config.MiniMsg;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class Strings {

    /**
     * Config strings
     */
    public static Component PREFIX = MiniMsg.mini("strings.prefix");
    public static Component SCOREBOARD_TITLE = MiniMsg.mini("strings.title");

    /**
     * Plain strings
     */
    public static Component CHARACTERS_SELECTION_NAME = MiniMsg.plain("Characters", RED);
    public static Component PERMISSION_REQUIRED = MiniMsg.plain("You have no permission to do that.", RED);
    public static Component ONLY_PLAYERS = MiniMsg.plain("You have to be a player to do that.", RED);
    public static Component UNKNOWN_COMMAND = MiniMsg.plain("Unknown command. ($command)", RED);
    public static Component NO_SETUP_STARTED = MiniMsg.plain("No setup started.", RED);
}
