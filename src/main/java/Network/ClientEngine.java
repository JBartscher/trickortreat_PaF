package main.java.Network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;

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


    // Movement
    private MovementManager.MovementType movementType;
    RadioButton radioButtonAWSD;
    RadioButton radioButtonARROW;
    RadioButton radioButtonMOUSE;

    public ClientEngine(GameLauncher gameLauncher, Stage stage) {
        this.gameLauncher = gameLauncher;
        this.stage = stage;
        showClientGUI();
    }

    public void showClientGUI() {

        Stage stageClient = new Stage();
        stageClient.setTitle("Connect to a Server");
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        TextField textFieldServer = new TextField("");
        Label label = new Label("Geben Sie die IP-Adresse des Servers ein:");
        textFieldServer.setPromptText("Geben Sie die IP-Adresse des Server ein: ");

        Label labelMovement = new Label("Select a Movement-Type:");

        final ToggleGroup group = new ToggleGroup();
        radioButtonAWSD = new RadioButton("KEYBOARD - AWSD");
        radioButtonARROW = new RadioButton("KEYBOARD - ARROW");
        radioButtonMOUSE = new RadioButton("MOUSE");
        radioButtonAWSD.setSelected(true);
        radioButtonAWSD.setToggleGroup(group);
        radioButtonARROW.setToggleGroup(group);
        radioButtonMOUSE.setToggleGroup(group);

        Button buttonCommit = new Button("JOINEN");

        buttonCommit.setOnAction( (e) -> {
            setMovementType();
            ip = textFieldServer.getText();
            stageClient.close();
            start();

        });

        vBox.getChildren().addAll(label, textFieldServer, labelMovement, radioButtonAWSD, radioButtonARROW, radioButtonMOUSE, buttonCommit);
        Scene scene = new Scene(vBox, 400, 300);
        stageClient.setScene(scene);
        stageClient.show();
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


    public void run() {

        joinServer();
        receiveFirstGameState();

        int index = 0;
        while(!ready) {
            index ++;
            if(index % 10000000 == 0) System.out.println("Warten");
        }


        communicate();
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

    @Override
    public void communicate() {
        System.out.println("Starte Kommunikation vom Client zum Server!");
        try{

            while(true) {

                // GameState über ObjectOutputStream an den Server verschicken
                networkController.sendMessage(output, gameState);

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

    public Game getGame() { return game; }

}