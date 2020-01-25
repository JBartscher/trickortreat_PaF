package main.java.gameobjects.mapobjects;

import main.java.Game;
import main.java.Sound;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.Tile;
import main.java.map.TileCollection;

/**
 * the house of alice cooper
 * this class also implements the interface -Accessible- that ensures the movement within the building
 */
public class Mansion extends House implements Accessible {

    public static final int MANSION_WIDTH = 5;
    public static final int MANSION_HEIGHT = 4;

    public Player insidePlayer = null;

    /**
     * create the mansion of Alice Cooper
     *
     * @param x
     * @param y
     */
    public Mansion(int x, int y) {
        super(x, y, MANSION_HEIGHT, MANSION_WIDTH);
        this.tileset = TileCollection.getMansionOutsideTiles();
    }

    /**
     * play music and join player to mansion
     *
     * @param player the player entity that visits the current house instance
     */
    @Override
    public void visit(Player player) {
        if (player.getProtectedTicks() > 0) return;
        if (isUnvisited()) {
            setInsideMode(player);
            Sound.playCooper();


        } else if (!isUnvisited() && player == insidePlayer) {
            setInsideMode(insidePlayer);
            if (Game.DRAMATIC) {
                Sound.playCountdown();
            } else {
                Sound.playMusic();
            }
        }

        notifyObservers(this);
    }


    public void updateMap() {

        Tile[][][] map = Map.getInstance().getMap();

        for (int y = 0; y < tileset[0].length; y++) {
            for (int x = 0; x < tileset.length; x++) {
                if (x == 0) {
                    map[x + this.getX()][y + this.getY()][2] = tileset[x][y];
                } else {
                    map[x + this.getX()][y + this.getY()][1] = tileset[x][y];
                }
            }
        }
        Map.getInstance().setMap(map);
    }


    /**
     * repaint House after visit
     */
    @Override
    public void repaintAfterVisit() {

        if(insidePlayer == null) {
            this.tileset = TileCollection.getMansionOutsideTiles();
        } else {
            this.tileset = TileCollection.getMansionInsideTiles();
        }
    }

    /**
     * set player attributes on inside mode ( no collision with houses )
     *
     * @param player
     */
    @Override
    public void setInsideMode(Player player) {
        if (!player.isInside()) {
            insidePlayer = player;
            isUnvisited = false;
            player.setNoCollision(true);
            player.setInside(true);
            player.setyPos(player.getyPos() + -Tile.TILE_SIZE * 1);
            player.setInsideObject(this);
            player.setProtectedTicks(25);
            this.tileset = TileCollection.getMansionInsideTiles();

        } else {
            insidePlayer = null;
            isUnvisited = true;
            player.setNoCollision(false);
            player.setInside(false);
            player.setyPos(player.getyPos() + Tile.TILE_SIZE * 1);
            player.setInsideObject(null);
            player.setProtectedTicks(25);
            this.tileset = TileCollection.getMansionOutsideTiles();
        }

    }

    public Player getInsidePlayer() {
        return insidePlayer;
    }

    public void setInsidePlayer(Player insidePlayer) {
        this.insidePlayer = insidePlayer;
    }

}