package main.java.gameobjects.mapobjects;

import main.java.Game;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.Tile;
import main.java.map.TileCollection;

import java.awt.*;

/**
 * this building is always located in the centre and contains a key when a child is currently captured
 */
public class TownHall extends House implements Accessible {

    public static final int TOWN_HALL_WIDTH = 5;
    public static final int TOWN_HALL_HEIGHT = 4;

    private boolean hasKey = false;

    private int numberOfPlayerInside;

    private EventType eventType;

    public enum EventType {
        KEY, VISITED
    }


    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_HEIGHT, TownHall.TOWN_HALL_WIDTH);
        this.tileset = TileCollection.getTownHallTiles();
    }

    @Override
    public void visit(Player player) {
        /**
         * The player can only visits the TownHall when protection is over
         *
         */
        if(player.getProtectedTicks() > 0 || (Game.DRAMATIC && !player.isInside())) return;
        setInsideMode(player);
        repaintAfterVisit();
        eventType = EventType.VISITED;
        notifyObservers(this);
    }


    /**
     * gives the key to the player and removes it from the townhall
     *
     * @param player the player taking the key
     */
    public void takeKey(Player player) {
        if(hasKey && !player.hasKey()) {
            player.setHasKey(true);
            this.hasKey = false;
            replaceKeyTableTile();
            repaintAfterVisit();
            eventType = EventType.KEY;
            notifyObservers(this);
        }
    }

    /**
     * replaces the tile which holds the key with an empty table
     */
    public void replaceKeyTableTile() {
        if (!this.hasKey) {
            this.tileset[1][2] = TileCollection.getMissingKeyTile();

        }
    }

    /**
     * transfer updated tiles to tile map
     */
    public void updateMap() {

        Tile[][][] map = Map.getInstance().getMap();

        for (int y = 0; y < tileset[0].length; y++) {
            for (int x = 0; x < tileset.length; x++) {
                if(x == 0) {
                    map[x + this.getX()][y + this.getY()][2] = tileset[x][y];
                } else {
                    map[x + this.getX()][y + this.getY()][1] = tileset[x][y];
                }
            }
        }
        Map.getInstance().setMap(map);
    }

    /**
     * paints the inside view of the townhall if the player is inside
     *
     *
     */
    @Override
    public void repaintAfterVisit() {
        if (numberOfPlayerInside > 0) {
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
            player.setyPos(player.getyPos() - 1.5 * Tile.TILE_SIZE);
            player.setTarget(new Point((int)player.getxPos(), (int)player.getyPos()));
            player.setInsideObject(this);
            player.setProtectedTicks(10);
            numberOfPlayerInside++;

        } else {
            player.setNoCollision(false);
            player.setInside(false);
            player.setyPos(player.getyPos() + 1.5 * Tile.TILE_SIZE);
            player.setInsideObject(null);
            player.setProtectedTicks(10);
            numberOfPlayerInside--;
        }
    }


    public boolean isHasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public int getNumberOfPlayerInside() {
        return numberOfPlayerInside;
    }

    public void setNumberOfPlayerInside(int numberOfPlayerInside) {
        this.numberOfPlayerInside = numberOfPlayerInside;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
