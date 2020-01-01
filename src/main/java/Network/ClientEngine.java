package main.java.Network;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            if(index % 100000 == 0) System.out.println("Warten");
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
            Platform.runLater( () -> {
                System.out.println(gameStateReceived);
                Game game = new Game(this, gameStateReceived, stage, movementType);


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

                //Thread.sleep(20);

                game = gameLauncher.getGame();
                Message message = new Message(Message.Type.GAMESTATE, game.getNetworkController().getGameState());
                output.writeObject(message);
                output.flush();

                Message msg = (Message)input.readObject();
                GameState newGameState = (GameState)msg.getObject();

                game.setMap(newGameState.getMap());
                game.setGameTime(newGameState.getGameTime());
                game.getOtherPlayer().setGameStateData(newGameState.getPlayerData());

                //newGameState = new GameState(game.getMap(), new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), game.getGameTime());
                newGameState = new GameState(null, new PlayerData(game.getOtherPlayer()), new PlayerData(game.getPlayer()), game.getGameTime());
                game.getNetworkController().setGameState(newGameState);
                this.gameState = newGameState;
            }

        } catch(Exception e) {
            System.out.println(e);

        }

    }

    public GameState getGameState() {
        return gameState;
    }
    public Game getGame() { return game; }



}
