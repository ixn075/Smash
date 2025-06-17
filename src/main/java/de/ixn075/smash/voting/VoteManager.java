package de.ixn075.smash.voting;

import de.ixn075.smash.map.Map;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VoteManager {

    private final HashMap<Player, Map> votes = new HashMap<>();

    public void vote(Player player, Map map) {
        if (!votes.containsKey(player)) {
            votes.put(player, map);
        } else {
            votes.replace(player, votes.get(player), map);
        }
    }

    public Map get(Player player) {
        return votes.get(player);
    }

    public void edit(Player player, Map newMap) {
        votes.replace(player, newMap);
    }
}
