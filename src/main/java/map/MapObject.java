package map;

import map.placing_utils.Placeble;

public abstract class MapObject {

    private Placeble placeble;

    public Placeble getPlaceble() {
        return placeble;
    }

    public abstract Tile getTileByTileIndex(int x, int y);
}
