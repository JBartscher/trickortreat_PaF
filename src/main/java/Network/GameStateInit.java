package main.java.Network;

import main.java.map.Map;

public class GameStateInit extends GameState {

    private static final long serialVersionUID = 1287629170864898744L;

    private Map map;

    public GameStateInit(Map map, PlayerData playerData, PlayerData otherPlayerData, WitchData witchData, CooperData cooperData, Event event, int gameTime) {
        super(playerData, otherPlayerData, witchData, cooperData, event, gameTime);
        this.map = map;

    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

}


