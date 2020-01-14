package main.java.Network;

import main.java.map.Map;

import java.util.ArrayList;

public class GameStateInit extends GameState {

    private static final long serialVersionUID = 1287629170864898744L;

    private Map map;
    private PlayerData playerData;
    private PlayerData otherPlayerData;

    private CooperData cooperData;
    private WitchData witchData;

    private ArrayList<Event> eventQueue = new ArrayList<>();
    private Event event;
    private boolean eventTransmitted = true;

    private int gameTime;

    public GameStateInit(Map map, PlayerData playerData, PlayerData otherPlayerData,WitchData witchData, CooperData cooperData, Event event, int gameTime) {
        super(playerData, otherPlayerData, witchData, cooperData, event, gameTime);
        this.map = map;
        this.playerData  = playerData;
        this.otherPlayerData = otherPlayerData;
        this.cooperData = cooperData;
        this.witchData = witchData;
        this.event = event;

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
        this.eventQueue.add(new Event(o, type));
        eventTransmitted = false;
        this.event = new Event(o, type);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }

    public ArrayList<Event> getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(ArrayList<Event> eventQueue) {

        this.eventQueue.clear();
        this.eventQueue.addAll(eventQueue);

        //this.eventQueue = eventQueue;
    }

    public void clearEventQueue() {
        eventQueue.clear();
        eventTransmitted = true;
    }
}
