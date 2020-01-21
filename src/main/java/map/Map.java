package main.java.map;

import main.java.Singleton;

import java.io.Serializable;
import java.util.Random;

public class Map implements Serializable, Singleton {

    private static final long serialVersionUID = 1L;
    private static Map instance;
    final Sector mapSector;
    private final int size;
    Tile[][][] map;


    //TODO: nicht schön, aber erstmal funktionell
    public static final int BORDER = 20;

    public Map(int size) {

        this.size = size;

        map = new Tile[size][size][3];

        // 2D Arrays throw ArrayStoreException if one tryes to fill them just with Arrays.fill([],val)
        Random r = new Random();

        // TODO: ERSTMAL HARDGECODET ZUR GENERIERUNG VON BIOMEN
        for(int z = 0; z < 3; z++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {

                    // z = 0 -> Layer 0 , entspricht den Bodentiles
                    if (z == 1) {

                        // Zentrumsteine generieren
                        if(x > size * 0.35 && x < size * 0.66 && y > size * 0.35 && y < size * 0.66) {
                            map[y][x][z] = new Tile(25);
                            map[y][x][0] = new Tile(25);

                            continue;
                        }

                        // Abtrennungen zwischen den Stadteilen - horizontal und vertikal
                        if ( (x == size / 3 || x == (size / 3 + 1) || y == size / 3 || y == (size / 3 + 1)   ) || (x == size * 2 / 3 || x == (size * 2 / 3 + 1) || y == size * 2/ 3 || y == (size * 2 / 3 + 1) ) )  {
                            map[y][x][z] = new Tile(20);
                            map[y][x][0] = new Tile(20);
                            continue;
                        }

                    }
                        map[y][x][z] = new Tile(0);

                }
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

    //TODO: beim Client ist die Map-Instance nicht gesetzt, führt zu Problemen beim colliden mit Türen, daher diese unschöne Lösung
    public static void setInstance(Map instance) {
        Map.instance = instance;
    }

    public int getSize() {
        return size;
    }

    public Sector getMapSector() {
        return mapSector;
    }

    public Tile[][][] getMap() {
        return map;
    }

    public void setMap(Tile[][][] newMap) {
        this.map = newMap;
    }
}