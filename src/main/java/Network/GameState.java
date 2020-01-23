package main.java.Network;

import java.io.Serializable;

/**
 * a Gamestate is a snapshot of the current game data and is used to synchronize between two network games
 * also the class needs to implement the Interface Serializable and Cloneable to ensure deep copies of the current game data
 */
public class GameState implements Serializable, Cloneable {

    private static final long serialVersionUID = 1287629170864898744L;

    protected PlayerData playerData;
    protected PlayerData otherPlayerData;

    protected CooperData cooperData;
    protected WitchData witchData;

    //private ArrayList<Event> eventQueue = new ArrayList<>();
    protected Event event;
    protected boolean eventTransmitted = true;

    protected int gameTime;

    /**
     * set the gamestate depending on current game data (positions, move directions ...
     */
    public GameState(PlayerData playerData, PlayerData otherPlayerData,WitchData witchData, CooperData cooperData, Event event, int gameTime) {
        this.playerData  = playerData;
        this.otherPlayerData = otherPlayerData;
        this.cooperData = cooperData;
        this.witchData = witchData;
        this.event = event;

        this.gameTime = gameTime;
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

    public EntityData getWitchData() {
        return witchData;
    }

    public int getGameTime() {
        return gameTime;
    }

    public boolean isEventTransmitted() {
        return eventTransmitted;
    }

    public void setEventTransmitted(boolean eventTransmitted) {
        this.eventTransmitted = eventTransmitted;
    }

    public CooperData getCooperData() {
        return cooperData;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}