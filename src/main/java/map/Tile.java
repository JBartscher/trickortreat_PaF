package main.java.map;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Properties;

public class Tile {
    boolean walkable;
    int tileNr;
    static final Properties tileColors = new Properties();
    public static final int TILE_SIZE = 64;


    public Tile(int n, boolean walkable) {
        this.tileNr = n;
        this.walkable = walkable;

        HashMap<Integer, Color> colorProperties = new HashMap<>();
        colorProperties.put(1, Color.GREEN); //Grass
        colorProperties.put(5, Color.DARKRED); //House
        colorProperties.put(6, Color.ORANGERED); //RichHouse
        colorProperties.put(7, Color.INDIANRED); //PoorHouse
        colorProperties.put(8, Color.BROWN); //Door
        tileColors.putAll(colorProperties);

        // tileColors.loadFromXML();
    }

    public int getTileNr() {
        return this.tileNr;
    }

    /**
     * return Color of Tile
     *
     * @return Color of Tile
     */
    public Color getTileColor() {
        return (Color) tileColors.getOrDefault(this.tileNr, Color.WHITE);
    }
}
