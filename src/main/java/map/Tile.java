package map;

public class Tile {
    boolean walkable;
    int tileNr;

    Tile(int n, boolean walkable) {
        this.tileNr = n;
        this.walkable = walkable;
    }
}
