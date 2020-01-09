package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class Mansion extends House implements Visitible {

    public static final int MANSION_WIDTH = 8;
    public static final int MANSION_HEIGHT = 5;

    public Player insidePlayer = null;

    /**
     * create the manison of Alice Cooper
     *
     * @param x
     * @param y
     */
    public Mansion(int x, int y) {
        super(x, y, MANSION_HEIGHT, MANSION_WIDTH);
        this.tileset = TileCollection.getMansonTiles();
    }

    @Override
    public void visit(Player player) {
        if(isUnvisited()) {
            setInsideMode(player);
        } else if (!isUnvisited() && player == insidePlayer) {

            setInsideMode(insidePlayer);
        }


        // super.visit(player);

        // play music
        // target[witch] => new
        // animate Alice Cooper?
    }


    @Override
    public void repaintAfterVisit() {

    }

    @Override
    public void setInsideMode(Player player) {
        if (!player.isInside()) {
            insidePlayer = player;
            isUnvisited = false;
            player.setNoCollision(true);
            player.setInside(true);
            player.setyPos(player.getyPos() + -Tile.TILE_SIZE);
            player.setInsideObject(this);

        } else {
            insidePlayer = null;
            isUnvisited = true;
            player.setNoCollision(false);
            player.setInside(false);
            player.setyPos(player.getyPos() + Tile.TILE_SIZE);
            player.setInsideObject(null);
        }

    }
}
