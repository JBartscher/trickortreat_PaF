package main.java;

import main.java.gameobjects.Player;
import main.java.map.Tile;

public class GameCamera {

    private int xOffSet, yOffSet;
    private int sizeX, sizeY;
    private Player player;

    public GameCamera(int sizeX, int sizeY, Player player) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.player = player;
    }

    public void centerOnPlayer(){
        xOffSet = (int) (player.getxPos() - Game.WIDTH / 2 + player.getSize() / 2);
        if(xOffSet < 0) {
            xOffSet = 0;
        } else {
            int t = sizeX * Tile.TILE_SIZE - (Game.WIDTH);
            if(xOffSet > t) {
                xOffSet = t;
            }
        }
        yOffSet = (int) (player.getyPos() - (Game.HEIGHT) / 2 + player.getSize() / 2);
        if(yOffSet < 0) {
            yOffSet = 0;
        } else {
            int t = (int)(sizeY * Tile.TILE_SIZE - Game.HEIGHT);
            if(yOffSet > t) {
                yOffSet = t;
            }
        }

        player.setxOffSet(xOffSet);
        player.setyOffSet(yOffSet);

    }

    public int getXOffset() {
        return xOffSet;
    }

    public int getYOffset() {
        return yOffSet;
    }
}
