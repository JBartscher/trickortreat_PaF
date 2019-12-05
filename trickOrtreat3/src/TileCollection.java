//TODO: Ich check Enums nicht :( Wenn jemand das durch einen Enum ersetzten könnte wäre das Töfte.

/**
 * The TileCollection holds all possible Tiles as a static reference.
 */
public class TileCollection {

    private final static int HOUSE = 5;
    public final static Tile HOUSE_TILE = new Tile(HOUSE, false);
    private final static int DOOR = 8;
    public final static Tile DOOR_TILE = new Tile(DOOR, false);
    private final static int GRASS = 1;
    public final static Tile GRASS_TILE = new Tile(GRASS, true);
    private final static int STREET = 2;
    public final static Tile STREET_TILE = new Tile(STREET, true);

}