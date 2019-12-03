package map;

import map.placing_utils.Placeble;

/**
 * Object which should be the base class of every object that is placed statically on the map, This provides the
 * possibility to implement generic placing logik.
 *
 * @see MapGenerator
 */
public abstract class MapObject {

    protected Placeble placeble;

    public Placeble getPlaceble() {
        return placeble;
    }

    public void setPlaceble(Placeble placeble) {
        this.placeble = placeble;
    }

    public abstract Tile getTileByTileIndex(int x, int y);

}
