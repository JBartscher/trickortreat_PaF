package main.java.Network;

import java.io.Serializable;
import java.util.ArrayList;

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

    public void setWitchData(WitchData witchData) {
        this.witchData = witchData;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
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

    public void setCooperData(CooperData cooperData) {
        this.cooperData = cooperData;
    }

    public void addEvent(Object o, Event.EventType type) {
        //this.eventQueue.add(new Event(o, type));
        eventTransmitted = false;
        this.event = new Event(o, type);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }

    //public ArrayList<Event> getEventQueue() {
     //   return eventQueue;
    //}

    public void setEventQueue(ArrayList<Event> eventQueue) {

        //this.eventQueue.clear();
        //this.eventQueue.addAll(eventQueue);

        //this.eventQueue = eventQueue;
    }

    public void clearEventQueue() {
        //eventQueue.clear();
        eventTransmitted = true;
    }


}