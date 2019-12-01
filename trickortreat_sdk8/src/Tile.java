public class Tile {
    boolean walkable;
    int tileNr;
    public static final int TILE_SIZE = 32;

    public Tile(int n, boolean walkable) {
        this.tileNr = n;
        this.walkable = walkable;
    }
}
