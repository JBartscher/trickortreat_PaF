package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class TownHall extends House implements Visitible {

    public static final int TOWN_HALL_WIDTH = 5;
    public static final int TOWN_HALL_HEIGHT = 4;

    private boolean hasKey = true;

    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_HEIGHT, TownHall.TOWN_HALL_WIDTH);
        this.tileset = TileCollection.getTownHallTiles();
    }

    @Override
    public void visit(Player player) {
        repaintAfterVisit(player);
        setInsideMode(player);
        notifyObservers(this);
    }

    @Override
    public void repaintAfterVisit() {
        //
    }

    //TODO: take key - collision with key

    /**
     * gives the key to the player and removes it from the townhall
     *
     * @param player the player taking the key
     */
    public void takeKey(Player player) {
        player.setHasKey(true);
        this.hasKey = false;
        replaceKeyTableTile();
    }

    /**
     * replaces the tile which holds the key with an empty table
     */
    public void replaceKeyTableTile() {
        if (!this.hasKey) {
            this.tileset[3][1] = TileCollection.getMissingKeyTile();
        }
    }

    /**
     * paints the inside view of the townhall if the player is inside
     *
     * @param player the visiting player
     */
    public void repaintAfterVisit(Player player) {
        if (!player.isInside()) {
            this.tileset = TileCollection.getTownHallInsideTiles();
            replaceKeyTableTile();
        } else {
            this.tileset = TileCollection.getTownHallTiles();
        }
    }

    /**
     * sets weather the player is inside the townhall or not
     *
     * @param player the visiting player
     */
    @Override
    public void setInsideMode(Player player) {
        if (!player.isInside()) {
            player.setNoCollision(true);
            player.setInside(true);
            player.setyPos(player.getyPos() + -Tile.TILE_SIZE);
            player.setInsideObject(this);

        } else {
            player.setNoCollision(false);
            player.setInside(false);
            player.setyPos(player.getyPos() + Tile.TILE_SIZE);
            player.setInsideObject(null);
        }
    }
}
