package main.java.network;

import main.java.map.Map;

/**
 * this gamestate represents a whole game (also the game map) and is needed to create a new game
 * while playing a game there is no need to always send all information to the other player
 *      ->  in this case the network controller uses the super class (gamestate) that does not contain the game map
 *
 */
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


