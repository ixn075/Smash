package de.ixn075.smash.voting;

import de.ixn075.smash.map.Map;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VoteManager {

    private final HashMap<Player, Map> votes = new HashMap<>();

    public boolean vote(Player player, Map map) {
        if (!votes.containsKey(player)) {
            votes.put(player, map);
            return true;
        } else {
            return false;
        }
    }

    public Map get(Player player) {
        return votes.get(player);
    }

    public void rem(Player player) {
        votes.remove(player);
    }
}
