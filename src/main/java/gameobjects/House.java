package gameobjects;

import map.Tile;
import map.TileCollection;
import map.placing_utils.Placeble;

import java.util.Arrays;

public class House {

    final int OFFSET = 1;

    private Tile[][] tileset;

    private Placeble placeble;

    public House(int x, int y, int tileWidth, int tileHeight) {
        this.tileset = new Tile[tileWidth][tileHeight];
        for (Tile[] row : this.tileset)
            Arrays.fill(row, TileCollection.HOUSE_TILE);
        this.placeble = new Placeble(x, y, tileWidth, tileHeight);
        setHouseDoorPosition();
    }

    public House(Placeble placeble) {
        this.tileset = new Tile[placeble.getWidth()][placeble.getHeight()];
        for (Tile[] row : this.tileset)
            Arrays.fill(row, TileCollection.HOUSE_TILE);
        this.placeble = placeble;
        setHouseDoorPosition();
    }

    public Placeble getPlaceble() {
        return placeble;
    }

    private void setHouseDoorPosition() {
        int center_cell = tileset.length / 2;
        int last_row = tileset[0].length - 1;

        tileset[last_row][center_cell] = TileCollection.DOOR_TILE;
    }

    public Tile getHouseTile(int r, int c) {
        return tileset[r][c];
    }
}
