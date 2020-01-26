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

/**
 * this class implements the Network interface and has the ability to communicate with a client
 */
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

    /**
     * thread task: create the network gui and start the server
     */
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

        /**
         * wait until gui is completely finished
         */
        while(!finished.get()) {
        }

        /**
         * start the server and wait for requests from clients
         */
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

            /**
             * wait for requests and assign incoming request to instance object: "socket"
             */
            Socket socket = serverSocket.accept();
            this.socket = socket;

            /**
             * an object of the private class "RequestHandler" is responsible to handle further communcations
             * start the handler and shutdown the current thread (there is no need to wait for more requests)
             */
            requestHandler = new RequestHandler(socket);
            requestHandler.start();
            this.interrupt();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Handler-class that communicates with the client by receiving and transmitting game states and events
     */
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

            /**
             * wait for the entering of the client
             */
            while(!ready) {
                System.out.println("Serverseitig warten");

            }
            communicate();
            if(ClientEngine.restart && ServerEngine.restart) initReplay();

        }

        /**
         * create a new gamestate and send it to the client
         * also start the game and show the gui
         */
        public void sendFirstGameStateToClient() {
            Platform.runLater(() -> {
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

        /**
         * create a new game and send the new game to the client
         */
        public void initReplay() {
            ready = false;
            ClientEngine.restart = false; ServerEngine.restart = false;
            initGameAndController();

            sendFirstGameStateToClient();

            /**
             * wait for the client
             */
            while(!ready) {
                System.out.println("Serverseitig warten");

            }
            communicate();
            if(ServerEngine.restart && ClientEngine.restart) initReplay();
        }
    }


    /**
     * create new game and game controller when a replay was requested by both players
     */
    public void initGameAndController() {
        game = new Game(gameLauncher, stage, Game.GameMode.REMOTE, ServerEngine.this, movementType, null);
        networkController = (NetworkController)game.getGameController();
        networkController.updateGameState(new GameStateInit(game.getMap(), new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), null, Game.TIME));

    }

    /**
     * this method controls the communication between server and client
     */
    @Override
    public void communicate() {

        System.out.println("Starte Kommunikation vom Server zum Client!");
        try{

            while(true) {

                /**
                 * stop the communication when the game is over and both players want a rematch
                 */
                if(ClientEngine.restart && ServerEngine.restart) return;
                Thread.sleep(10);

                /**
                 * read message, extract the gamestate and update the own gamestate
                 */
                Message msg = (Message)input.readObject();
                GameState gameStateReceived = msg.getGameState();
                updateServerData(gameStateReceived);

                /**
                 * handle incoming events and update the gamestate
                 */
                if(msg.getMessageType() == Message.Type.EVENT) {
                    networkController.handleEvents(gameStateReceived);
                }


                /**
                 * determine the current gamestate and send it to the other player
                 */
                GameState newGameState = new GameState(new PlayerData(game.getPlayer()), new PlayerData(game.getOtherPlayer()), new WitchData(game.getWitch()), new CooperData(game.getAliceCooper()), gameState.getEvent(), game.getGameTime());
                networkController.updateGameState(newGameState);
                // neuen GameState nach Überprüfung verschicken
                networkController.sendMessage(output, newGameState);
            }

        } catch(Exception e) {

        }
    }

    /**
     * update the game data when receiving a gamestate from client
     * @param gameStateReceived
     */
    private void updateServerData(GameState gameStateReceived) {
        game.getOtherPlayer().setGameStateData(gameStateReceived.getOtherPlayerData());
    }

    public void setGameState(GameState gameState) {

        this.gameState = gameState;
    }


    public Game getGame() { return game; }

    /**
     * stop the handler when the connection is lost
     */
    public void stopHandler() {
        if(requestHandler != null) {
            requestHandler.interrupt();
            requestHandler = null;
        }
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

}