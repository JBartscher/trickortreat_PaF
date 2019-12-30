package main.java.map;

import java.util.Random;

public class Map {

    private static Map instance;
    final Sector mapSector;
    private final int size;
    Tile[][] map;

    public Map(int size) {

        this.size = size;

        map = new Tile[size][size];

        // 2D Arrays throw ArrayStoreException if one tryes to fill them just with Arrays.fill([],val)
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Tile(r.nextInt(4) + 1);
            }
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