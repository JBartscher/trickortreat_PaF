package main.java.map;

import java.util.Arrays;

public class Map {

    private static Map instance;
    final Sector mapSector;
    private final int size;
    Tile[][] map;

    public Map(int size) {

        this.size = size;

        map = new Tile[size][size];
        try {
            // 2D Arrays throw ArrayStoreException if one tryes to fill them just with Arrays.fill([],val)
            for (Tile[] row : this.map)
                Arrays.fill(row, TileCollection.GRASS_TILE);
        } catch (ArrayStoreException ex) {
            ex.printStackTrace();
        }

        mapSector = new Sector(0, 0, size, size);

        Map.instance = this;
    }

    public static Map getInstance() {
        if (Map.instance == null)
            Map.instance = new Map(Map.getInstance().getSize());
        return Map.instance;
    }

    public int getSize() {
        return size;
    }

    public Sector getMapSector() {
        return mapSector;
    }

    public Tile[][] getMap() {
        return map;
    }

    public void setMap(Tile[][] newMap) {
        this.map = newMap;
    }
}