package main.java.Network;

import main.java.map.Map;
import main.java.map.MapObject;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map map;
    private PlayerData playerData;
    private PlayerData otherPlayerData;

    private CooperData cooperData;
    private EntityData witchData;

    private MapObject eventObj;
    private boolean eventTransmitted = true;

    public MapObject getEvent() {
        return eventObj;
    }

    public void setEvent(MapObject event) {
        this.eventObj = event;
    }

    private int gameTime;

    public GameState(Map map, PlayerData playerData, PlayerData otherPlayerData, CooperData cooperData, EntityData witchData, MapObject event, int gameTime) {
        this.map = map;
        this.playerData  = playerData;
        this.otherPlayerData = otherPlayerData;
        this.cooperData = cooperData;
        this.witchData = witchData;
        this.eventObj = event;

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

    public CooperData getCooperData() {
        return cooperData;
    }

    public void setCooperData(CooperData cooperData) {
        this.cooperData = cooperData;
    }

    public EntityData getWitchData() {
        return witchData;
    }

    public void setWitchData(EntityData witchData) {
        this.witchData = witchData;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public MapObject getEventObj() {
        return eventObj;
    }

    public void setEventObj(MapObject eventObj) {
        this.eventObj = eventObj;
    }

    public boolean isEventTransmitted() {
        return eventTransmitted;
    }

    public void setEventTransmitted(boolean eventTransmitted) {
        this.eventTransmitted = eventTransmitted;
    }


}
