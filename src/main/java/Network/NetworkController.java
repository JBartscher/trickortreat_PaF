package main.java.Network;

import main.java.*;
import main.java.Menu.GameOver;
import main.java.gameobjects.mapobjects.House;
import main.java.map.Map;
import main.java.map.MapObject;

import java.io.ObjectOutputStream;
import java.util.List;


// aktualisiert und delegiert den aktuellen GameState zur eingesetzten Engine (Client o. Server)
// GameStates werden in dieser Klasse aktualisiert
public class NetworkController extends GameController {

    public enum NetworkRole {
        SERVER, CLIENT
    }

    private NetworkRole networkRole;

    // repräsentiert eine Instanz, die das Interface Network implementiert (communicate-method)
    // ClientEngine und ServerEngine implementieren das Interface
    private Network networkEngine;
    private GameState gameState;

    public NetworkController(Game game, Network networkEngine, NetworkRole networkRole) {
        super(game);
        this.networkEngine = networkEngine;
        this.networkRole = networkRole;
    }



    public NetworkRole getNetworkRole() {
        return networkRole;
    }

    public Network getNetworkEngine() {
        return networkEngine;
    }

    public GameState getGameState() {
        return gameState;
    }

    /**
     * vergleicht eigenen GameState mit dem erhaltenen u. berechnet korrekten GameState
     * @param newGameState
     */
    public void updateGameState(GameState newGameState) {

        if(gameState == null) gameState = newGameState;

        if(!gameState.isEventTransmitted()) {
            Event event = gameState.getEvent();
            gameState = newGameState;
            gameState.setEventTransmitted(false);
            gameState.setEvent(event);
        } else {
            gameState = newGameState;
            gameState.setEvent(null);
            gameState.setEventTransmitted(true);
        }

        networkEngine.setGameState(gameState);
    }

    public void createAndUpdateGameState() {
        GameState newGameState = new GameState(new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());
        updateGameState(newGameState);

    }

    public void changeGameStateObject (Object o, Event.EventType type) {

        // Event setzen u. Übermittlung auf false setzen
        gameState.setEvent(new Event(o, type));
        gameState.setEventTransmitted(false);

        // Weiterleiten an NetworkEngine
        networkEngine.setGameState(gameState);
    }

    public void sendMessage(ObjectOutputStream output, GameState gameState) {
        Message.Type type;

        if(!gameState.isEventTransmitted()) {
            type = Message.Type.EVENT;
        } else {
            type = Message.Type.GAMESTATE;
        }

        try {
            output.writeObject(Message.deepCopy(new Message(type, gameState)));
            output.flush();
            clearAllEvents();
            networkEngine.setGameState(gameState);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearAllEvents () {
        gameState.setEvent(null);
        gameState.setEventTransmitted(true);
    }

    public void handleEvents(GameState gameStateReceived){
        Event event = gameStateReceived.getEvent();
        if(event == null) return;

        switch (event.getType()) {

            case VISITED:

                // Über alle Objekte iterieren und Objekt updatensdd
                List<MapObject> mapObjects = Map.getInstance().getMapSector().getAllContainingMapObjects();
                for (MapObject obj : mapObjects) {
                    MapObject eventMapObject = (MapObject) event.getObject();
                    if ((obj.getX() == eventMapObject.getX() && obj.getY() == eventMapObject.getY()) || (obj == eventMapObject)) {
                        House h = (House) obj;

                        System.out.println("EVENT-Obj " + h);

                        h.setUnvisited(((House) eventMapObject).isUnvisited());
                        h.repaintAfterVisit();
                        h.updateMap();
                        game.getOtherPlayer().notifyObservers(game.getOtherPlayer());

                    }
                }
                break;

            case COLLISION:
                //Witch witch = (Witch)(gameStateReceived.getEvent().getObject());
                //game.setWitch(witch);
                game.getWitch().setGameStateData(gameStateReceived.getWitchData());

                break;


            case PAUSED:
                game.paused = true;
                break;
            case UNPAUSED:
                game.paused = false;
                break;
            case REPLAY:
                System.out.println("EVENT REPLAY!");
                if(networkRole == NetworkRole.SERVER) {
                    ClientEngine.restart = true;
                    if(!ServerEngine.restart) {
                        GameOver.setMessage("Client wants a replay!");
                    }

                } else if(networkRole == NetworkRole.CLIENT) {
                    ServerEngine.restart = true;
                    if(!ClientEngine.restart) {
                        GameOver.setMessage("Server wants a replay!");
                    }
                }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        Event.EventType eventType;

        if(o instanceof House) {
            eventType = Event.EventType.VISITED;
        } else {
            eventType = Event.EventType.COLLECTED;
        }

        changeGameStateObject(o, eventType);
    }

    @Override
    protected GameCamera setGameCameraEnemy() {

        game.getListOfPlayers().remove(game.getOtherPlayer());

        Game.WIDTH = Window.WIDTH;
        System.out.println(Game.WIDTH);
        return null;
    }
}

