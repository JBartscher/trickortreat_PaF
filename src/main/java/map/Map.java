package map;

public class Map {

    int size_x;
    int size_y;

    Tile[][] map;

    public Map(int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;

        map = new Tile[size_x][size_y];
    }
}
