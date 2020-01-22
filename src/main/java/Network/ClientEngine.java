package main.java.Network;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;
import main.java.MovementManager.MovementType;

import java.io.*;
import java.net.Socket;

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

    public void run() {

        joinServer();
        receiveFirstGameState();

        int index = 0;
        while(!ready) {
            index ++;
            if(index % 10000000 == 0) System.out.println("Warten");
        }

        communicate();
        System.out.println("AUFRUF CLIENT REPLAY");
        if(ServerEngine.restart && ClientEngine.restart) initReplay();
    }

    public void joinServer() {
        try {
            socket = new Socket(ip, ServerEngine.PORT);

            output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                game = new Game(this, gameStateReceived, stage, movementType);
                networkController = (NetworkController)game.getGameController();


                //TODO: beim Client ist die Map-Instance nicht gesetzt, führt zu Problemen beim colliden mit Türen, daher diese unschöne Lösung
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

    public void initReplay() {
        System.out.println("AUFRUF CLIENT REPLAY");
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

    @Override
    public void communicate() {
        System.out.println("Starte Kommunikation vom Client zum Server!");
        try{

            while(true) {


                // GameState über ObjectOutputStream an den Server verschicken
                networkController.sendMessage(output, gameState);

                if(ClientEngine.restart && ServerEngine.restart) { return; }

                // GameState vom Server lesen
                Message msg = (Message)input.readObject();
                GameState gameStateReceived = msg.getGameState();

                // Eigene Daten anhand des erhaltenen GameStates aktualisieren
                updateClientData(gameStateReceived);

                // Erhaltenen GameState auf Events überprüfen und ggf. behandeln
                if(msg.getMessageType() == Message.Type.EVENT) {
                    networkController.handleEvents(gameStateReceived);
                }

                // neuen GameState ermitteln, im Controller updaten und alles delegieren
                networkController.createAndUpdateGameState();
            }

        } catch(Exception e) {
            System.out.println(e);

        }
    }

    // Eigene Daten an den neuen GameState anpassen
    public void updateClientData(GameState gameStateReceived) {
        game.setGameTime(gameStateReceived.getGameTime());
        game.getOtherPlayer().setGameStateData(gameStateReceived.getPlayerData());
        game.getWitch().setGameStateData(gameStateReceived.getWitchData());
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Game getGame() { return game; }



}