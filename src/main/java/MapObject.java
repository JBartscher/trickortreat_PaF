/**
 * Object which should be the base class of every object that is placed statically on the map, This provides the
 * possibility to implement generic placing logic.
 *
 * @see MapGenerator
 */
public abstract class MapObject {

    protected Placeable placeable;

    public Placeable getPlaceable() {
        return placeable;
    }

    public void setPlaceable(Placeable placeable) {
        this.placeable = placeable;
    }

    public abstract Tile getTileByTileIndex(int x, int y);


}
