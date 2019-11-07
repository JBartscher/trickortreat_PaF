package map;

import java.util.List;

public class Map {

    int size_x;
    int size_y;

    Tile[][] map;

    List<Object> mapObjects;

    public Map(int size) {
        this.size_x = size;
        this.size_y = size;

        map = new Tile[size_x][size_y];
    }
}
