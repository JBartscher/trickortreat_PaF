package main.java.network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import main.java.*;
import main.java.configuration.Configuration;
import main.java.menu.GameOver;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.Mansion;
import main.java.gameobjects.mapobjects.TownHall;
import main.java.map.Map;
import main.java.map.MapObject;
import main.java.pattern.Observable;
import main.java.sounds.Sound;

import java.io.ObjectOutputStream;
import java.util.List;


/**
 * this class updates the current gamestate and delegates the updates to his networkEngine
 * also this class is responsible for creating, transmitting and handling events
 */
public class NetworkController extends GameController {

    private final static Configuration<Object> config = new Configuration<Object>();

    /**
     * the role in a network engine is either the server role or the role as a client
     */
    public enum NetworkRole {
        SERVER, CLIENT
    }

    private NetworkRole networkRole;

    /**
     * networkEngine is either a client instance or server instance
     * each instance implements the interface Network which ensures the ability to communicate with other computers
     */
    private Network networkEngine;
    private GameState gameState;

    public NetworkController(Game game, Network networkEngine, NetworkRole networkRole, GameLauncher gameLauncher) {
        super(game, gameLauncher);
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
     * compare two gamestates and determine / set the newest one - also registered non transmitted events
     * ensure that a event that is not already transmitted will not overwritten
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

    /**
     * create and update a GameState
     */
    public void createAndUpdateGameState() {
        GameState newGameState = new GameState(new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());
        updateGameState(newGameState);
    }

    /**
     * called when a event occurred -> set that event and set Event transmitting to false
     * @param o
     * @param type
     */
    public void changeGameStateObject (Object o, Event.EventType type) {

        /**
         * set event and set transmitting to false (send it with the next iteration)
         */

        gameState.setEvent(new Event(o, type));
        gameState.setEventTransmitted(false);

        /**
         * update gamestate of network engine
         */
        networkEngine.setGameState(gameState);
    }

    /**
     * create a deep copy of the gamestate and send it to the other player
     * @param output
     * @param gameState
     */
    public void sendMessage(ObjectOutputStream output, GameState gameState) {
        Message.Type type;

        if(!gameState.isEventTransmitted()) {
            type = Message.Type.EVENT;
            System.out.println("Inner: OBJEKT:" + gameState.getEvent().getObject());
        } else {
            type = Message.Type.GAMESTATE;
        }

        try {
            /**
             * create deep copy and send message to other player
             */
            Message msg = Message.deepCopy(new Message(type, gameState));
            output.writeObject(msg);
            output.flush();
            if(msg.getGameState().getEvent() == null && gameState.getEvent() != null)
                changeGameStateObject(gameState.getEvent().getObject(), gameState.getEvent().getType());
            else
                clearAllEvents();
            
            networkEngine.setGameState(gameState);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all events after sending the events to the other player
     */
    public void clearAllEvents () {
        gameState.setEvent(null);
        gameState.setEventTransmitted(true);
    }

    /**
     * handle incoming events from other player
     * @param gameStateReceived
     */
    public void handleEvents(GameState gameStateReceived){
        Event event = gameStateReceived.getEvent();
        if(event == null) return;
        List<MapObject> mapObjects = Map.getInstance().getMapSector().getAllContainingMapObjects();
        switch (event.getType()) {

            case VISITED:
                /** find event object and update data and/or repaint house
                 *
                 */
                for (MapObject obj : mapObjects) {
                    MapObject eventMapObject = (MapObject) event.getObject();
                    if ((obj.getX() == eventMapObject.getX() && obj.getY() == eventMapObject.getY()) || (obj == eventMapObject)) {
                        House h = (House) obj;

                        System.out.println("EVENT-Obj " + h);

                        h.setUnvisited(((House) eventMapObject).isUnvisited());
                        if(h instanceof Mansion && eventMapObject instanceof Mansion) {
                            Mansion m = (Mansion)h;
                            m.setInsidePlayer(((Mansion)eventMapObject).getInsidePlayer());
                        }
                        h.repaintAfterVisit();
                        h.updateMap();
                        game.getOtherPlayer().notifyObservers(game.getOtherPlayer());
                    }
                }
                break;

            case COLLISION:
                game.getWitch().setGameStateData(gameStateReceived.getWitchData());
                System.out.println("EVENT KIND GEFANGEN " + game.getWitch().isOnReturn());
                if(networkRole == NetworkRole.SERVER) {
                    game.getWitch().setOnReturn(true);
                }


                break;

            case PAUSED:
                game.paused = true;
                config.setParam("paused", game.paused);
                /**
                 * show menu when receiving an event of type "paused"
                 */
                Platform.runLater( () -> {
                    game.getLauncher().getMainMenu().showPausedMenu(game.getWindow().getScene());
                });
                break;
            case UNPAUSED:
                System.out.println("UNPAUSED ERHALTEN!!");
                game.paused = false;
                config.setParam("paused", game.paused);
                /**
                 * resume the game when receiving an event of type "unpaused"
                 */
                Platform.runLater( () -> {
                    game.getLauncher().getMainMenu().resumeGame(game);
                });

                break;
            /**
             * called when the other player wants to play again and transmitted an event with type "REPLAY"
             */
            case REPLAY:
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
            /**
             * called when the other player visited the townhall or collected the key
             */
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
             * called when a player collides with door of gingerbread house
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

    /**
     * update the model data when receiving changes from the observables
     * @param o   Observable Object which called notifyObservers method
     * @param arg not used
     */
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
    public void shutDownNetwork() {
        super.shutDownNetwork();

        /**
         * check if currently a connection is available otherwise shutdown without popup window
         */
        if( networkEngine instanceof ServerEngine ) {
            if( ((ServerEngine)networkEngine).socket == null)
                return;

        }
            Platform.runLater(() -> {
                Light.Distant light = new Light.Distant();
                light.setAzimuth(0);
                Lighting lighting = new Lighting(light);
                lighting.setSurfaceScale(5.0);
                Stage errorStage = new Stage();
                Label labelError = new Label("LOST CONNECTION TO OTHER PLAYER - SHUTDOWN NETWORK");
                labelError.setStyle("-fx-text-fill: white; -fx-font-size: 1.25em; ");
                VBox vBox = new VBox(10);
                vBox.setEffect(lighting);
                vBox.setStyle("-fx-background-color: black;");
                vBox.setAlignment(Pos.CENTER);
                Scene scene = new Scene(vBox, 550, 120);
                Button buttonClose = new Button("EXIT TO DESKTOP");
                buttonClose.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0;" +
                "-fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");
                vBox.getChildren().addAll(labelError, buttonClose);
                errorStage.setScene(scene);
                errorStage.initStyle(StageStyle.UNDECORATED);
                errorStage.initModality(Modality.APPLICATION_MODAL);
                errorStage.show();

                buttonClose.setOnAction( (e) -> {
                    Sound.playMenu();

                    errorStage.close();
                    stopGameLoop();
                    Platform.exit();
                    //gameLauncher.getMainMenu().showMainMenu();

    
                });
            });


    }

    /**
     * remove the second game camera when playing in a network game -> there is no need to render the second player separately
     * @return
     */
    @Override
    protected GameCamera setGameCameraEnemy() {
        game.getListOfPlayers().remove(game.getOtherPlayer());

        Game.WIDTH = Window.WIDTH;
        System.out.println(Game.WIDTH);
        return null;
    }
}

