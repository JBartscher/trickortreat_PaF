import java.util.Arrays;

public class Map {

    private int size_x;
    private int size_y;

    Tile[][] map;

    Sector mapSector;

    DistrictManager sectorManager;

    public Map(int size) {
        this.size_x = size;
        this.size_y = size;

        map = new Tile[size_x][size_y];
        try {
            // 2D Arrays throw ArrayStoreException if one tryes to fill them just with Arrays.fill([],val)
            for (Tile[] row : this.map)
                Arrays.fill(row, TileCollection.GRASS_TILE);
        } catch (ArrayStoreException ex) {
            ex.printStackTrace();
        }

        mapSector = new Sector(0, 0, size_x, size_y);
        // TODO vielleicht so? mapSector = new Sector(0, size_y, size_x, size_y);
    }

    int getSize_x() {
        return size_x;
    }

    int getSize_y() {
        return size_y;
    }

    Sector getMapSector() {
        return mapSector;
    }

    public Tile[][] getMap() {
        return map;
    }
}