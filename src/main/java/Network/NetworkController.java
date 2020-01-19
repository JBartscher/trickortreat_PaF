package main.java.Network;

import main.java.*;
import main.java.Menu.GameOver;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.TownHall;
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
        List<MapObject> mapObjects = Map.getInstance().getMapSector().getAllContainingMapObjects();
        switch (event.getType()) {

            case VISITED:
                // Über alle Objekte iterieren und Objekt updatensdd
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
                break;
            case TOWNHALL:
                for (MapObject obj : mapObjects) {
                    TownHall eventMapObject = (TownHall) event.getObject();
                    if (obj instanceof TownHall) {
                        /**
                         * other player visited the townhall -> redraw
                         */
                        if(eventMapObject.getEventType() == TownHall.EventType.VISITED) {
                            //System.out.println("ANZAHL SPIELER INTERN: " + ((TownHall) obj).getNumberOfPlayerInside() + " BEKOMMEN: " + eventMapObject.getNumberOfPlayerInside());
                            ((TownHall)obj).setNumberOfPlayerInside(((TownHall) eventMapObject).getNumberOfPlayerInside());
                            ((TownHall)eventMapObject).repaintAfterVisit();
                            System.out.println(((TownHall) obj).getNumberOfPlayerInside());

                            ((TownHall)obj).repaintAfterVisit();
                            ((TownHall)obj).updateMap();

                        } else if(eventMapObject.getEventType() == TownHall.EventType.KEY) {
                            ((TownHall)obj).setHasKey(false);
                            ((TownHall)obj).repaintAfterVisit();
                            if(((TownHall)obj).getNumberOfPlayerInside() > 0) {
                                game.getMap().getMap()[29][31][1].setTileNr(133);
                            }
                        }
                    }
                }
                break;

            /**
             * called when a player collides with door of gingerbreahouse
             * -> update gingerbread house, children count, spawn key
             */
            case KIDNAPPING:
                System.out.println("KIDNAPPING!");
                TownHall t = null;
                GingerbreadHouse g = null;
                GingerbreadHouse receivedHouse = (GingerbreadHouse)event.getObject();

                /**
                 * iterate over all map objects and search for town hall and gingerbread house
                 * set local variables and check for right gingerbread house
                 */
                for(MapObject o : game.getMap().getMapSector().getAllContainingMapObjects()) {
                    if(o instanceof TownHall) {
                        t = (TownHall)o;
                    }
                    if(o instanceof GingerbreadHouse && o.getX() == receivedHouse.getX() && o.getY() == receivedHouse.getY()) {
                        g = (GingerbreadHouse)o;
                    }
                }

                /**
                 * update data of gingerbread house: graphics, hasChild:bool
                 */
                g.setHasChild((receivedHouse.isHasChild()));
                g.repaintAfterVisit();
                g.updateMap();

                t.setHasKey(true);
                t.repaintAfterVisit();
                t.updateMap();
                if(t.getNumberOfPlayerInside() > 0 ) {
                    game.getMap().getMap()[29][31][1].setTileNr(120);
                }


                break;


        }
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        Event.EventType eventType;

        if(o instanceof TownHall) {
            eventType = Event.EventType.TOWNHALL;
        } else if(o instanceof GingerbreadHouse) {
            eventType = Event.EventType.KIDNAPPING;
        } else if(o instanceof House) {
            eventType = Event.EventType.VISITED;
        } else {
            eventType = Event.EventType.VISITED;
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

