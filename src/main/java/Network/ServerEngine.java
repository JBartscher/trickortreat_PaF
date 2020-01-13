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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private NetworkController networkController;

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
                networkController = (NetworkController)game.getGameController();
                networkController.updateGameState(new GameState(game.getMap(), new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), null, Game.TIME));

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

                Message message = new Message(Message.Type.INIT, ((NetworkController)game.getGameController()).getGameState());
                gameState = ((NetworkController)game.getGameController()).getGameState();
                try {
                    output.writeObject(Message.deepCopy(message));
                    output.flush();
                    stageNetwork.close();
                    gameLauncher.startGame(game);
                    game.getWindow().showGameGUI();

                    System.out.println("Sende ersten GameState zum Client");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ready = true;
            });
        }
    }

    // Diese Methode repräsentiert die Kommunikation zwischen Client u. Server
    @Override
    public void communicate() {

        System.out.println("Starte Kommunikation vom Server zum Client!");
        try{

            while(true) {

                long start = System.currentTimeMillis();
                Thread.sleep(20);

                // Nachricht vom Client lesen, in GameState konvertieren u. eigenen GameState aktualisieren
                Message msg = (Message)input.readObject();
                GameState gameStateReceived = msg.getGameState();

                // eigene Daten aktualisieren
                updateServerData(gameStateReceived);

                // Auf Events überprüfen und gegebenfalls verarbeiten
                if(msg.getMessageType() == Message.Type.EVENT) {
                    networkController.handleEvents(gameStateReceived);
                }

                // Neuen GameState ermitteln u. überprüfen (sind noch eigene Events nicht verschicken wurden?)
                GameState newGameState = new GameState(null, new PlayerData(game.getPlayer()), new PlayerData(game.getOtherPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());
                networkController.updateGameState(newGameState);

                // neuen GameState nach Überprüfung verschicken
                networkController.sendMessage(output, newGameState);

                //System.out.println(System.currentTimeMillis() - start);
            }

        } catch(Exception e) {

        }
    }

    private void updateServerData(GameState gameStateReceived) {
        game.getOtherPlayer().setGameStateData(gameStateReceived.getOtherPlayerData());

        //TODO: EVALUIEREN : der Server braucht an sich keine Aktualisierung der HEXE, weil der Server die HEXE steuern soll- außer bei Kollision?
        //game.getWitch().setGameStateData(gameState.getWitchData());
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Game getGame() { return game; }

}