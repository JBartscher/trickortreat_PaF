package main.java;

import main.java.Network.PlayerData;
import main.java.map.Map;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map map;
    private PlayerData playerData;
    private PlayerData otherPlayerData;
    private int gameTime;

    public GameState(Map map, PlayerData playerData, PlayerData otherPlayerData, int gameTime) {
        this.map = map;
        this.playerData  = playerData;
        this.otherPlayerData = otherPlayerData;
        this.gameTime = gameTime;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public PlayerData getOtherPlayerData() {
        return otherPlayerData;
    }

    public void setOtherPlayerData(PlayerData otherPlayerData) {
        this.otherPlayerData = otherPlayerData;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }


}
