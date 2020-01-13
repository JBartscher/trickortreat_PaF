package main.java;

import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.House;
import main.java.map.Tile;

import java.util.Arrays;

public class GameController implements Observer {


    protected Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public GameController() {

    }


    public void initGUIandSound(Stage stage) {
        // GUI-Bereich
        game.setWindow(new Window(game, stage));
        game.getWindow().createGUI();
        game.setMapRenderer(new MapRenderer(game.getMap(), game.getWindow(), game));
        game.setGameCamera(new GameCamera(game.getMap().getSize(), game.getMap().getSize(), game.getPlayer()));
        game.setMovementManager(new MovementManager(game, game.getPlayer(), game.getOtherPlayer()));

        // Movement - Weiterleiten an Controller-Klasse
        game.getWindow().getScene().addEventHandler(InputEvent.ANY, game.getMovementManager());

        try {
            //Sound.playMusic();
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

    }

    public void initNetwork() {

    }

    public void initEntities(MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {

        game.setPlayer(new Player(movementTypePlayer1));
        game.getListOfPlayers().add(game.getPlayer());

        game.setOtherPlayer(new Player(movementTypePlayer2));
        game.getListOfPlayers().add(game.getOtherPlayer());
        Game.WIDTH = Window.WIDTH / 2 - Tile.TILE_SIZE;
        game.setGameCameraEnemy(setGameCameraEnemy());


        game.getOtherPlayer().setyPos(game.getPlayer().getxPos());
        game.getOtherPlayer().setyPos(game.getPlayer().getyPos() + 5 * Tile.TILE_SIZE);

        game.setWitch(new Witch());

        game.getWitch().setxPos(game.getMap().getSize() * Tile.TILE_SIZE - Tile.TILE_SIZE);
        game.getWitch().setxPos(game.getMap().getSize() * Tile.TILE_SIZE - Tile.TILE_SIZE);

        game.setAliceCooper(new AliceCooper());

        game.getListOfAllEntites().addAll(Arrays.asList(game.getPlayer(), game.getOtherPlayer(), game.getWitch()));

    }

    public void initObservers() {

    }

    protected GameCamera setGameCameraEnemy() {
        return new GameCamera(game.getMap().getSize(), game.getMap().getSize(), game.getOtherPlayer());
    }

    public NetworkController.NetworkRole getNetworkRole() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        House h = (House)o;
        h.repaintAfterVisit();
        h.updateMap();


    }
}
