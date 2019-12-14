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

    public abstract Tile getTileByTileIndex(int x, int y);


}
