package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.MapObject;
import main.java.map.Tile;

public class House extends MapObject {

    /**
     * Offset of Object to other Objects @see Placeble
     */
    final int OFFSET = 1;

    protected Tile[][] tileset;

    private Tile doorTile;

    boolean isUnvisited = true;

    private District district = null;

    /**
     * House constructor.
     *
     * fills the whole tileset with House-Tiles and creates a Door-Tile at a suitable place.
     * @param x x coordinate on game map
     * @param y y coordinate on game map
     * @param tileWidth width
     * @param tileHeight height
     */
    public House(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = new Tile[tileWidth][tileHeight];
    }


    /**
     * Sets the Tile at the last Row in the relative center of the House to a House Tile Object and sets a reference
     * to this Tile.
     *
    private void setHouseDoorPosition() {
        int last_row = tileset.length - 1;
        int center_cell = tileset[0].length / 2;

        tileset[last_row][center_cell] = TileCollection.DOOR_TILE;
        this.doorTile = tileset[last_row][center_cell];
    }
     */

    /**
     * gets an Tile of the House by its row and column index
     * @param r row index
     * @param c column index
     * @return the tile that is indexed by r and c
     */
    public Tile getTileByTileIndex(int r, int c) {
        try {
            return tileset[r][c];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    /**
     * method that should be called when a player visits a house. Special houses like the town hall or witch house
     * override this method.
     *
     * @param player the player entity that visits the current house instance
     * @see TownHall
     */
    public void visit(Player player) {
        System.out.println("VISITED THIS HOUSE! " + this);
        if (isUnvisited) {
            player.addCandy((int) this.district.getCandy_multiplikator() * player.getChildrenCount());
        }
        System.out.println(player.getCandy());
        this.isUnvisited = false;
    }
}