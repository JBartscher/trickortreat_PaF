package main.java.Network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;
import main.java.MovementManager.MovementType;

import java.io.*;
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

    public static boolean restart;

    // Movement
    private MovementManager.MovementType movementType;

    public ServerEngine(GameLauncher gameLauncher, Stage stage, MovementType movementType) {
        this.gameLauncher = gameLauncher;
        this.stage = stage;
        this.movementType = movementType;
        start();
    }

    @Override
    public void run () {

        Platform.runLater( () -> {

            vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            this.labelRequests = new Label();
            vBox.getChildren().addAll(labelRequests);
            stageNetwork = new Stage();
            Scene scene = new Scene(vBox, 350, 30);
            stageNetwork.setScene(scene);
            stageNetwork.show();

        });

        AtomicBoolean finished = new AtomicBoolean(false);
        finished.set(true);

        // Thread am Leben halten, bis der Button gedrückt wurde
        while(!finished.get()) {
        }

        startServer();
    }

    // Erstellt bereits ein Game-Objekt
    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            Platform.runLater( () -> {

                labelRequests.setText("Server wurde gestartet - PORT " + PORT + " - warte auf Client");
                initGameAndController();

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
                output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                output.flush();
                input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
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
            if(ClientEngine.restart && ServerEngine.restart) initReplay();

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

        public void initReplay() {
            System.out.println("SERVER AUFRUF REPLAY!");

            ready = false;
            ClientEngine.restart = false; ServerEngine.restart = false;
            initGameAndController();

            sendFirstGameStateToClient();

            // Wartet auf die Initalisierung der GUI
            while(!ready) {
                System.out.println("Serverseitig warten");

            }
            communicate();
            if(ServerEngine.restart && ClientEngine.restart) initReplay();
        }
    }


    public void initGameAndController() {
        game = new Game(gameLauncher, stage, Game.GameMode.REMOTE, ServerEngine.this, movementType, null);
        networkController = (NetworkController)game.getGameController();
        networkController.updateGameState(new GameStateInit(game.getMap(), new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), null, Game.TIME));

    }

    // Diese Methode repräsentiert die Kommunikation zwischen Client u. Server
    @Override
    public void communicate() {

        System.out.println("Starte Kommunikation vom Server zum Client!");
        try{

            while(true) {

                long start = System.currentTimeMillis();
                Thread.sleep(10);

                if(ClientEngine.restart && ServerEngine.restart) return;

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
                GameState newGameState = new GameState(new PlayerData(game.getPlayer()), new PlayerData(game.getOtherPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());
                networkController.updateGameState(newGameState);
                //GameState newGameState = new GameState(null, null, null, null, null, 20);

                // neuen GameState nach Überprüfung verschicken
                networkController.sendMessage(output, newGameState);

                //System.out.println("Dauer einer Verbindung:" + (System.currentTimeMillis() - start));
            }

        } catch(Exception e) {

        }
    }

    public long sizeOf(Serializable object){
        if (object == null) {
            return 0;
        }

        try{
            final ByteCountingOutputStream out = new ByteCountingOutputStream();
            new ObjectOutputStream(out).writeObject(object);
            out.close();
            return out.size();
        }catch (IOException e){

            return -1;
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

    public GameState getGameState() {
        return gameState;
    }

    public Game getGame() { return game; }

}