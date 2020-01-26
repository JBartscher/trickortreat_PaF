package main.java.map;

/**
 * Object which should be the base class of every object that is placed statically on the map, This provides the
 * possibility to implement generic placing logic.
 *
 * @see MapGenerator
 */
public abstract class MapObject extends Placeable {

    public MapObject(int x, int y, int width, int height, int offset) {
        super(x, y, width, height, offset);
    }

    public MapObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * this constructor is only here so the House decorator can have an own constructor, which only accepts an house
     * instance.
     */
    public MapObject() {
        super();
    }

    /**
     * meant to give MapObjects the option to return specific tiles
     *
     * @param x x position
     * @param y y position
     * @return Tile at that position
     */
    public abstract Tile getTileByTileIndex(int x, int y);


}
