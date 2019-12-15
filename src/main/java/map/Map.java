package main.java.map;

import java.util.Arrays;

public class Map {

    final Tile[][] map;
    final Sector mapSector;
    private final int size_x;
    private final int size_y;

    public Map(int size) {
        this.size_x = size;
        this.size_y = size;

        map = new Tile[size_x][size_y];
        try {
            // 2D Arrays throw ArrayStoreException if one tryes to fill them just with Arrays.fill([],val)
            for (Tile[] row : this.map)
                Arrays.fill(row, TileCollection.GRASS_TILE);
        } catch (ArrayStoreException ex) {
            ex.printStackTrace();
        }

        mapSector = new Sector(0, 0, size_x, size_y);
        // TODO vielleicht so? mapSector = new Sector(0, size_y, size_x, size_y);
    }

    public int getSize_x() {
        return size_x;
    }

    public int getSize_y() {
        return size_y;
    }

    public Sector getMapSector() {
        return mapSector;
    }

    public Tile[][] getMap() {
        return map;
    }
}