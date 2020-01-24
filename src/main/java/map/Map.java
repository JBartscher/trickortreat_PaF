package main.java.map;

import main.java.Singleton;

import java.awt.*;
import java.io.Serializable;

/**
 * each game contains a map with tiles and sectors
 * this class encapsulates all map data and implements the singleton-pattern
 */
public class Map implements Serializable, Singleton {

    private static final long serialVersionUID = 1L;
    private static Map instance;
    final Sector mapSector;
    private final int size;
    Tile[][][] map;

    public final static Point xTopLeftCentre = new Point(21, 21);
    public final static Point xTopRightCentre = new Point(40, 21);
    public final static Point xBottomLeftCentre = new Point(21, 40);
    public final static Point xBottomRightCentre = new Point(40, 40);


    public Map(int size) {

        this.size = size;

        map = new Tile[size][size][3];

        mapSector = new Sector(0, 0, size, size);
        Map.instance = this;
    }



    /**
     * enesure that always one map instance is available
     * @return: map instance
     */
    public static Map getInstance() {
        if (Map.instance == null)
            Map.instance = new Map(Map.getInstance().getSize());

        return Map.instance;
    }

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