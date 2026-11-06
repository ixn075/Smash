package de.ixn075.smash.voting;

import de.ixn075.smash.api.map.Map;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class VoteManager {

    private final HashMap<UUID, Vote> votes = new HashMap<>();

    public Vote vote(@NonNull UUID uuid, Map map) {
        Vote vote = new Vote(map);
        if (!votes.containsKey(uuid)) {
            votes.put(uuid, vote);
        } else {
            votes.replace(uuid, vote);
        }
        return votes.get(uuid);
    }

    public Vote getVote(UUID uuid) {
        return votes.get(uuid);
    }

    public void unvote(UUID uuid) {
        votes.remove(uuid);
    }
}
