package main.java;

import main.java.map.Tile;

public class GameCamera {

    private int xOffSet, yOffSet;
    private int sizeX, sizeY;

    public GameCamera(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void centerOnEntity(final Entity e){
        xOffSet = (int) (e.getxPos() - Window.WIDTH / 2 + e.getSize() / 2);
        if(xOffSet < 0) {
            xOffSet = 0;
        } else {
            int t = sizeX * Tile.TILE_SIZE - (Window.WIDTH);
            if(xOffSet > t) {
                xOffSet = t;
            }
        }
        yOffSet = (int) (e.getyPos() - Window.HEIGHT / 2 + e.getSize() / 2);
        if(yOffSet < 0) {
            yOffSet = 0;
        } else {
            int t = sizeY * Tile.TILE_SIZE - (Window.HEIGHT);
            if(yOffSet > t) {
                yOffSet = t;
            }
        }
    }

    public int getXOffset() {
        return xOffSet;
    }

    public int getYOffset() {
        return yOffSet;
    }
}
