package main.java.Network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;
import main.java.gameobjects.mapobjects.House;
import main.java.map.MapObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerEngine extends Thread implements Network {

    private ServerSocket serverSocket;
    private Socket socket;
    private Stage stage;
    private GameLauncher gameLauncher;
    public static final int PORT = 30000;

    // Thread, der den Austausch der GameStates bearbeitet
    private RequestHandler requestHandler;
    private GameState gameState;

    // Socket-Ströme zum Austauschen des GameStates
    private ObjectOutputStream output;
    private ObjectInputStream input;

    // Warten auf Initalisierung der GUI
    public boolean ready = false;

    // Server GUI
    private Stage stageNetwork;
    VBox vBox;
    Label labelRequests;
    private Game game;

    // Movement
    private MovementManager.MovementType movementType;
    RadioButton radioButtonAWSD;
    RadioButton radioButtonARROW;
    RadioButton radioButtonMOUSE;


    public ServerEngine(GameLauncher gameLauncher, Stage stage) {
        this.gameLauncher = gameLauncher;
        this.stage = stage;
        start();
    }

    @Override
    public void run () {

        Button buttonHost = new Button("HOST GAME");

        Platform.runLater( () -> {

            stage.setTitle("Trick or Treat - Server");

            vBox = new VBox(10);

            vBox.setAlignment(Pos.CENTER);

            Label labelTitle = new Label("Game Configurations");

            Label labelMovement = new Label("- Select a Movement TYPE");

            final ToggleGroup group = new ToggleGroup();
            radioButtonAWSD = new RadioButton("KEYBOARD - AWSD");
            radioButtonARROW = new RadioButton("KEYBOARD - ARROW");
            radioButtonMOUSE = new RadioButton("MOUSE");
            radioButtonAWSD.setSelected(true);
            radioButtonAWSD.setToggleGroup(group);
            radioButtonARROW.setToggleGroup(group);
            radioButtonMOUSE.setToggleGroup(group);

            this.labelRequests = new Label();
            vBox.getChildren().addAll(labelTitle, labelMovement, radioButtonAWSD, radioButtonARROW, radioButtonMOUSE, buttonHost, labelRequests);

            stageNetwork = new Stage();
            stageNetwork.setTitle("Host a Game");
            Scene scene = new Scene(vBox, 600, 400);
            stageNetwork.setScene(scene);
            stageNetwork.show();

        });

        AtomicBoolean finished = new AtomicBoolean(false);
        buttonHost.setOnAction( (e) -> {
            finished.set(true);

        });

        // Thread am Leben halten, bis der Button gedrückt wurde
        while(!finished.get()) {

        }
        setMovementType();
        startServer();

    }

    public void setMovementType() {
        if(radioButtonARROW.isSelected()) {
            movementType = MovementManager.MovementType.KEYBOARD_ARROW;
        } else if(radioButtonAWSD.isSelected()) {
            movementType = MovementManager.MovementType.KEYBOARD_AWSD;
        } else if(radioButtonMOUSE.isSelected()) {
            movementType = MovementManager.MovementType.MOUSE;
        }

    }

    // Erstellt bereits ein Game-Objekt
    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            Platform.runLater( () -> {

                labelRequests.setText("Server wurde gestartet - PORT " + PORT + " - warte auf Client");
                game = new Game(gameLauncher, stage, Game.GameMode.REMOTE, ServerEngine.this, movementType, null);
                game.getNetworkController().setGameState(new GameState(game.getMap(), new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new EntityData(game.getWitch()), new CooperData(game.getAliceCooper()), null, Game.TIME));

            });

            // Wartet auf Verbindungen , instanziert einen Thread für Requests und übersendet in diesem Thread den GameState
            Socket socket = serverSocket.accept();
            this.socket = socket;

            // private Klasse bearbeitet fortan die Kommunikation zwischen Client und Server
            requestHandler = new RequestHandler(socket);
            requestHandler.start();

            // Server-Thread, der auf eine Verbindung gewartet hat, wird nicht mehr benötigt und unterbrochen
            this.interrupt();


        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Diese Methode repräsentiert die Kommunikation zwischen Client u. Server
    @Override
    public void communicate() {

        System.out.println("Starte Kommunikation vom Server zum Client!");
        try{

            //ArrayList<Event> eventQueue = new ArrayList<>();
            while(true) {

                Thread.sleep(20);
                long start = System.currentTimeMillis();


                // Nachricht vom Server lesen, in GameState konvertieren u. eigenen GameState aktualisieren
                Message msg = (Message)input.readObject();
                GameState gameStateReceived = msg.getGameState();

                game.getOtherPlayer().setGameStateData(gameStateReceived.getOtherPlayerData());

                // Eigene Alice-Cooper Daten nur ändern, wenn sich was geändert hat (verfügbar und spielt aktuell nichts)
                //if(game.getAliceCooper().isAvailable())
                //    game.getAliceCooper().setGameStateData(gameStateReceived.getCooperData());


                // Es ist ein Event aufgetreten (Haus wurde besucht)
                if(msg.getMessageType() == Message.Type.EVENT) {
                    System.out.println("EVENT erhalten vom CLIENT - ");
                    handleEvents(gameStateReceived);
                }

                //TODO: EVALUIEREN : der Server braucht an sich keine Aktualisierung der HEXE, weil der Server die HEXE steuern soll- außer bei Kollision?
                //game.getWitch().setGameStateData(gameState.getWitchData());

                // Sofern ein Haus betreten

                Message.Type messageType;

                // Es existiert ein Event, was noch nicht transmitted wurde, MapObject (Haus) abholen
                if(!gameState.isEventTransmitted()) {
                    messageType = Message.Type.EVENT;
                    gameState.setEventTransmitted(true);
                    System.out.println(gameState.getEventQueue());
                } else {
                    messageType = Message.Type.GAMESTATE;
                    //gameState.clearEventQueue();
                    //game.getNetworkController().getGameState().clearEventQueue();
                    gameState.clearEventQueue();
                    game.getNetworkController().getGameState().clearEventQueue();
                    gameState.setEvent(null);
                    game.getNetworkController().getGameState().setEvent(null);

                }

                // Neuen GameState setzen und an Client verschicken

                GameState newGameState = new GameState(null, new PlayerData(game.getPlayer()), new PlayerData(game.getOtherPlayer()), new EntityData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());


                // GameState ersetzen, aber noch offene Events nicht verwerfen
                if(!gameState.isEventTransmitted()) {
                    Event event = this.gameState.getEvent();
                    game.getNetworkController().setGameState(newGameState);
                    game.getNetworkController().getGameState().setEvent(event);
                }

                // Verschicken
                output.writeObject(new Message(messageType, this.gameState));
                game.getNetworkController().setGameState(newGameState);
                output.flush();

                //System.out.println("Dauer einer Verbindung: " + (System.currentTimeMillis() - start) + " ms");

            }

        } catch(Exception e) {

        }
    }


    private class RequestHandler extends Thread {
        private Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            System.out.println("VERBINDUNG EINGEGANGEN!");
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }


            Platform.runLater(() -> {
                labelRequests.setText("Verbindung eingegangen von " + socket.getRemoteSocketAddress());
            });
            sendFirstGameStateToClient();

            // Wartet auf die Initalisierung der GUI
            while(!ready) {
                System.out.println("Serverseitig warten");

            }
            communicate();

        }

        public void sendFirstGameStateToClient() {

            Platform.runLater(() -> {
                // Instanziert ein neues Game-Objekt - sendet GameState zum Client

                Message message = new Message(Message.Type.INIT, game.getNetworkController().getGameState());
                gameState = game.getNetworkController().getGameState();
                try {
                    output.writeObject(message);
                    output.flush();
                    stageNetwork.close();
                    gameLauncher.startGame(game);
                    game.getWindow().showGameGUI();

                    System.out.println("Sende ersten GameState zum Client");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ready = true;
            });
        }

        public GameState getGameState() {
            return gameState;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Game getGame() { return game; }


    public void handleEvents(GameState gameStateReceived) {

        Event event = gameStateReceived.getEvent();
        if(event == null) return;

            switch(event.getType()) {

                case VISITED:
                    System.out.println((MapObject)(event.getObject()));
                    // Über alle Objekte iterieren und Objekt updatensdd
                    List mapObjects = game.getMapRenderer().getMap().getMapSector().getAllContainingMapObjects();
                    for(Object o : mapObjects ) {
                        MapObject obj = (MapObject)o;
                        MapObject eventMapObject = (MapObject)event.getObject();

                        //TODO: KEINE SCHÖNE LÖSUNG, aber funktioniert - komischerweise hat Client die gleichen Häuser wie der Server, aber andersrum nicht?...
                        if(  (obj.getX() == eventMapObject.getX() && obj.getY() == eventMapObject.getY()) || ( obj == eventMapObject) ) {
                            House h = (House)o;
                            h.repaintAfterVisit();
                            h.updateMap();
                            h.setUnvisited(false);
                        }
                    }
            }
    }

}