package map;

import map.placing_utils.Placeble;

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
