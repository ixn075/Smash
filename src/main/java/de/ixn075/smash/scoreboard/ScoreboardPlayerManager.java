package de.ixn075.smash.scoreboard;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class ScoreboardPlayerManager {

    private final List<Player> players = new LinkedList<>();

    public List<Player> getPlayers() {
        return players;
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }
}
