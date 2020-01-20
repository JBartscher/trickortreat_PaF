package main.java;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.Menu.GameMenu;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.Tile;
import main.java.sprites.GraphicsUtility;

import java.util.concurrent.TimeUnit;

public class MapRenderer {

    private Map map;
    private Window window;
    private Tile[][][] tileMap;
    private Game game;

    public MapRenderer(Map map, Window window, Game game) {
        this.map = map;
        this.tileMap = map.getMap();
        this.window = window;
        this.game = game;
        GraphicsUtility.initGraphics();
    }

    public static String calculateTime(Game game) {

        int gameTime = game.getGameTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(gameTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(gameTime - minutes * 1000 * 60);
        String secondsString = String.valueOf(seconds);
        if (seconds < 10) {
            secondsString = "0" + seconds;
        }

        return minutes + ":" + secondsString;
    }

    public void drawMap() {

        Group root = window.getRoot();
        root.getChildren().clear();
        GameCamera gameCamera = game.getGameCamera();
        Player player = game.getPlayer();
        Player otherPlayer = game.getOtherPlayer();

        Canvas canvas = new Canvas(Window.WIDTH, Window.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);



        /**
         * render the map for every player (one iteration on a network game, two iterations on a locale game)
         */
        int widthOffset = 0;
        for (Player playerObj : game.getListOfPlayers()) {
            if (widthOffset > 0) {
                // Lokale GameCamera austauschen (Rendern des zweiten Screens)
                gameCamera = game.getGameCameraEnemy();
            }

            // Karte rendern - verschieben in x Richtung, sofern Spieler 2 (LOKAL)
            for (int z = 0; z < 3; z++) {

                /**
                 * checks if current Layer is Layer 2 (overlapping objects
                 * draw player between Layer 1 and 2
                 */
                if(z == 2 && !game.getPlayer().isInside() && !game.getOtherPlayer().isInside()) {
                    drawPlayer(gameCamera, player, widthOffset, gc);
                    drawPlayer(gameCamera, otherPlayer, widthOffset, gc);
                    drawEntity(gc, game.getWitch(), gameCamera, widthOffset, 0, 0, 1, game.getWitch().getEntityImage());
                }

                /**
                 * draw every tile depending on tile nr
                 */
                for (int y = 0; y < tileMap.length; y++) {
                    for (int x = 0; x < tileMap[y].length; x++) {
                        int xPos = x * Tile.TILE_SIZE - gameCamera.getXOffset() + widthOffset;
                        int yPos = (int) (y * Tile.TILE_SIZE - gameCamera.getYOffset() + Window.HEIGHT * 0.1);
                        // nur Zeichnen, wenn sichtbar - sonst verwerfen

                        if (tileMap[y][x][z].getTileNr() == 0) continue;

                        /**
                         * check if position is within the current viewport
                         */
                        if (yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE * 2 && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {

                            /**
                             * set effect when player has no children
                             */
                            if(playerObj.getChildrenCount() == 0) {
                                gc.setGlobalAlpha(0.7);
                            } else {
                                gc.setGlobalAlpha(1.0);
                            }

                            Image image = GraphicsUtility.getTileImage(tileMap[y][x][z].getTileNr());
                            gc.drawImage(image, xPos, yPos, Tile.TILE_SIZE, Tile.TILE_SIZE);
                        }
                    }
                }

                if(game.getPlayer().isInside() || game.getOtherPlayer().isInside()) {
                    drawPlayer(gameCamera, player, widthOffset, gc);
                    drawPlayer(gameCamera, otherPlayer, widthOffset, gc);
                    drawEntity(gc, game.getWitch(), gameCamera, widthOffset, 0, 0, 1, game.getWitch().getEntityImage());
                }
            }


            Rectangle middleTile = null;
            if (widthOffset == 0) {
                otherPlayer = game.getOtherPlayer();
            } else {
                otherPlayer = game.getPlayer();
                middleTile = new Rectangle(Game.WIDTH, Window.HEIGHT * 0.1, 2 * Tile.TILE_SIZE, Window.HEIGHT);

            }


            /**
             * add a split screen when playing locale
             */
            if (middleTile != null)
                gc.fillRect(Game.WIDTH, Window.HEIGHT * 0.1, 2 * Tile.TILE_SIZE, Window.HEIGHT);
                //root.getChildren().add(middleTile);


            if(playerObj.hasKey()) {
                gc.drawImage(GraphicsUtility.getKeyImage(), widthOffset, Window.HEIGHT * 0.1, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
            widthOffset += Game.WIDTH + 2 * Tile.TILE_SIZE;
        }

        /**
         * add message when game is currently paused
         */
        if (game.paused) {
            Text textPaused = new Text("PAUSED");
            Rectangle rect = new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
            rect.setOpacity(0.3);
            GraphicsUtility.setTextProperties(textPaused, "-fx-font: 128 arial;", Color.DARKRED, Window.WIDTH / 2 - 250, 400.0);
            root.getChildren().addAll(textPaused, rect);

        }
        /**
         * Singleton GameMenu-JavaFx Group, welche alle InGameMenu Elemente hält, und so nicht in jeder draw() neu initialisiert werden muss
         */

        GameMenu.getInstance().updateTimeText(calculateTime(game));
        GameMenu.getInstance().addGameMenuToScene(root);
    }


    /**
     * draws the player (and his children) on the canvas
     */
    private void drawPlayer(GameCamera gameCamera, Player playerObj, int widthOffset, GraphicsContext gc) {
        // Eigenen Spieler und Anhang zeichnen
        for (int i = 0; i < playerObj.getChildrenCount(); i++) {
            Image imagePlayer = playerObj.getEntityImage();


            double xPos = playerObj.getxPos() - gameCamera.getXOffset() + widthOffset;
            double yPos = playerObj.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1;

            double xPosOffset = 0;
            double yPosOffset = 0;

            /**
             * change x/y coordinates depending on current child
             */
            if (i == 1) {
                imagePlayer = playerObj.getEntityImage2(); xPosOffset = 0.33 * Tile.TILE_SIZE;
            }
            if (i == 2) {
                imagePlayer = playerObj.getEntityImage3(); yPosOffset = 0.33 * Tile.TILE_SIZE;
            }
            if (i == 3) {
                imagePlayer = playerObj.getEntityImage4(); xPosOffset = 0.33 * Tile.TILE_SIZE; yPosOffset = 0.33 * Tile.TILE_SIZE;
            }
            if( i == 4) {
                imagePlayer = playerObj.getEntityImage5(); yPosOffset = 0.66 * Tile.TILE_SIZE;
            }

            if( i == 5) {
                imagePlayer = playerObj.getEntityImage5(); xPosOffset = 0.33 * Tile.TILE_SIZE; yPosOffset = 0.66 * Tile.TILE_SIZE;
            }

            drawEntity(gc, playerObj, gameCamera, widthOffset, xPosOffset, yPosOffset, 0.5, imagePlayer);

            //gc.drawImage(imagePlayer, xPos, yPos, 32, 32);
        }
    }

    /**
     * draws an entity on canvas (npc)
     */
    public void drawEntity(GraphicsContext gc, Entity entity, GameCamera gameCamera, int widthOffset, double xPosOffset, double yPosOffset, double scaleFactor, Image image) {


        double xPos = entity.getxPos() - gameCamera.getXOffset() + widthOffset + xPosOffset;
        double yPos = entity.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1 + yPosOffset;

        if (yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {

            int size = (int) (Tile.TILE_SIZE * scaleFactor);
            gc.drawImage(image, xPos, yPos, size, size);

        }

    }

}