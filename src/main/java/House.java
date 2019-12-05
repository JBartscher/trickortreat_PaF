

import java.util.Arrays;

public class House extends MapObject {

    /**
     Offset of Object to other Objects @see Placeble
     */
    final int OFFSET = 1;

    private Tile[][] tileset;

    private Tile doorTile;

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
        this.tileset = new Tile[tileWidth][tileHeight];
        for (Tile[] row : this.tileset)
            Arrays.fill(row, TileCollection.HOUSE_TILE);
        this.placeable = new Placeable(x, y, tileWidth, tileHeight);
        setHouseDoorPosition();
    }

    /**
     * House constructor in which all coordiantes are set by its placeble Object.
     *
     * @param placeable Placeble
     */
    public House(Placeable placeable) {
        this.tileset = new Tile[placeable.getWidth()][placeable.getHeight()];
        for (Tile[] row : this.tileset)
            Arrays.fill(row, TileCollection.HOUSE_TILE);
        this.placeable = placeable;
        setHouseDoorPosition();
    }

    /**
     * gets the placeble of the House instance.
     *
     * @return Placeble
     */
    public Placeable getPlaceable() {
        return placeable;
    }

    /**
     * Sets the Tile at the last Row in the relative center of the House to a House Tile Object and sets a refrence
     * to this Tile.
     */
    private void setHouseDoorPosition() {
        int center_cell = tileset.length / 2;
        int last_row = tileset[0].length - 1;

        tileset[last_row][center_cell] = TileCollection.DOOR_TILE;
        this.doorTile = tileset[last_row][center_cell];
    }

    /**
     * gets an Tile of the House by its row and column index
     * @param r row index
     * @param c column index
     * @return the tile that is indexed by r and c
     */
    public Tile getTileByTileIndex(int r, int c) {
        try{
        return tileset[r][c];}
        catch (ArrayIndexOutOfBoundsException e){
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
        int new_candy = (int) this.district.getCandy_multiplikator() * player.getChildrenCount();
        player.addCandy(new_candy);
    }
}
