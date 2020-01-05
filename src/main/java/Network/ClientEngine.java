package main.java.Network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.Message;
import main.java.MovementManager;
import main.java.map.MapObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientEngine extends Thread implements Network {

    private GameLauncher gameLauncher;
    private Socket socket;
    private Stage stage;
    private String ip;

    private Game game;
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
            //System.out.println("WARTEN");
            if(index % 1000000 == 0) System.out.println("Warten");
        }


        communicate();
    }

    public void joinServer() {
        try {
            socket = new Socket(ip, ServerEngine.PORT);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFirstGameState() {

        try {
            Message msg = (Message)input.readObject();
            GameState gameStateReceived = (GameState)msg.getObject();
            this.gameState = gameStateReceived;
            Platform.runLater( () -> {
                System.out.println(gameStateReceived);
                game = new Game(this, gameStateReceived, stage, movementType);


                //TODO: beim Client ist die Map-Instance nicht gesetzt, führt zu Problemen beim colliden mit Türen, daher diese unschöne Lösung

                game.getMap().setInstance(game.getMap());



                //this.map = gameStateReceived.getMap();


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

                // Sofern ein Haus betreten wurde
                MapObject mapObject = null;
                Message.Type messageType = null;
                if(!gameState.isEventTransmitted()) {
                    messageType = Message.Type.EVENT;
                    mapObject = gameState.getEventObj();
                    gameState.setEventTransmitted(true);
                } else {
                    messageType = Message.Type.GAMESTATE;
                }

                System.out.println(messageType);

                Message message = new Message(messageType, game.getNetworkController().getGameState());
                output.writeObject(message);
                output.flush();

                Message msg = (Message)input.readObject();
                GameState newGameState = (GameState)msg.getObject();

                // Daten an den neuen GameState anpassen
                game.setMap(newGameState.getMap());
                game.setGameTime(newGameState.getGameTime());
                game.getOtherPlayer().setGameStateData(newGameState.getPlayerData());
                game.getAliceCooper().setGameStateData(newGameState.getCooperData());
                game.getWitch().setGameStateData(newGameState.getWitchData());


                //TODO: FEHLERHAFT....

                // Es ist ein Event aufgetreten
                if(msg.getMessageType() == Message.Type.EVENT) {

                    List mapObjects = game.getMapRenderer().getMap().getMapSector().getAllContainingMapObjects();

                    // Über alle Objekte iterieren und Objekt updatensdd

                    int index = 0;
                    for(Object o : mapObjects ) {
                        if(o == gameState.getEventObj()) {
                            System.out.println(o);
                            System.out.println(gameState.getEventObj());
                            mapObjects.set(index, o);
                            index++;
                        }
                    }
                }

                newGameState = new GameState(null, new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), new CooperData(game.getAliceCooper()), new EntityData(game.getWitch()), mapObject, game.getGameTime());
                game.getNetworkController().setGameState(newGameState);
                this.gameState = newGameState;
            }

        } catch(Exception e) {
            System.out.println(e);

        }

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public GameState getGameState() {
        return gameState;
    }
    public Game getGame() { return game; }



}
