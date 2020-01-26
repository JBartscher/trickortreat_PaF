package main.java.Network;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;
import main.java.MovementManager.MovementType;

import java.io.*;
import java.net.Socket;

/**
 * this class implements the Network interface and has the ability to communicate with a server
 */
public class ClientEngine extends Thread implements Network {

    private GameLauncher gameLauncher;
    private Socket socket;
    private Stage stage;
    private String ip;

    private Game game;
    private NetworkController networkController;
    private GameState gameState;

    private boolean ready;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public static boolean restart;

    // Movement
    private MovementManager.MovementType movementType;

    public ClientEngine(GameLauncher gameLauncher, Stage stage, MovementType movementType, String ip) {
        this.gameLauncher = gameLauncher;
        this.stage = stage;
        this.movementType = movementType;
        this.ip = ip;
        start();
    }

    /**
     * thread task: join the server, receive the gamestate and start the communication with the server
     */
    public void run() {

        joinServer();
        receiveFirstGameState();

        int index = 0;
        while(!ready) {
            index ++;
            if(index % 10000000 == 0) System.out.println("Warten");
        }
        communicate();

        /**
         * if both players want to play again then init a rematch
         */
        if(ServerEngine.restart && ClientEngine.restart) initReplay();
    }

    /**
     * join the server based on the IP and PORT
     */
    public void joinServer() {
        try {
            socket = new Socket(ip, ServerEngine.PORT);

            /**
             * use the decorator pattern to ensure low latency with puffer
             */
            output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the first gamestate from puffer and create a game with the received data
     */
    public void receiveFirstGameState() {

        try {
            Message msg = (Message)input.readObject();

            while(msg.getGameState().getClass() == GameState.class) {
                msg = (Message)input.readObject();
            }

            GameStateInit gameStateReceived = (GameStateInit)(msg.getGameState());
            this.gameState = gameStateReceived;
            Platform.runLater( () -> {
                System.out.println(gameStateReceived);
                game = new Game(this, gameStateReceived, stage, movementType, gameLauncher);
                networkController = (NetworkController)game.getGameController();
                game.getMap().setInstance(game.getMap());

                gameLauncher.startGame(game);
                System.out.println("GameState vom Server erhalten");

                ready = true;

            });

        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * init a replay when both players agreed
     */
    public void initReplay() {
        ready = false;
        ClientEngine.restart = false;
        ServerEngine.restart = false;

        receiveFirstGameState();

        int index = 0;
        while(!ready) {
            index ++;
            if(index % 10000000 == 0) System.out.println("Warten");
        }


        communicate();
        if(ServerEngine.restart && ClientEngine.restart) initReplay();
    }

    /**
     * this method controls the communication between server and client
     */
    @Override
    public void communicate() {
        System.out.println("Starte Kommunikation vom Client zum Server!");
        try{

            while(true) {

                /**
                 * send the message via sockets to the server
                 */
                networkController.sendMessage(output, gameState);

                if(ClientEngine.restart && ServerEngine.restart) { return; }

                /**
                 * read the gamestate from server and update the own one
                 */
                Message msg = (Message)input.readObject();
                GameState gameStateReceived = msg.getGameState();
                updateClientData(gameStateReceived);

                /**
                 * handle incoming events and create the new gamestate
                 */
                if(msg.getMessageType() == Message.Type.EVENT) {
                    networkController.handleEvents(gameStateReceived);
                }
                networkController.createAndUpdateGameState();
            }

        } catch(Exception e) {
            System.out.println(e);

        }
    }

    /**
     * update data depending on the received gamestate
     * @param gameStateReceived
     */
    public void updateClientData(GameState gameStateReceived) {
        game.setGameTime(gameStateReceived.getGameTime());
        game.getOtherPlayer().setGameStateData(gameStateReceived.getPlayerData());
        game.getWitch().setGameStateData(gameStateReceived.getWitchData());
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    public Game getGame() { return game; }

}