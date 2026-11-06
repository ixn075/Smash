package de.ixn075.smash.voting;

import de.ixn075.smash.api.map.Map;

public class Vote {

    private Map map;

    public Vote(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
