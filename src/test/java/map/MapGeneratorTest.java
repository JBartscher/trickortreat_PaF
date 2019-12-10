package map;

import main.java.map.Map;
import main.java.map.MapGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapGeneratorTest {

    private MapGenerator mapGenerator;
    private Map gameMap;

    @BeforeEach
    void setUp() {
        this.gameMap = new Map(50);
        this.mapGenerator = new MapGenerator(gameMap);
    }

    @Test
    void testCreateMap() {
        long startTime = System.currentTimeMillis();
        mapGenerator.createMap();
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("time needed %s for map creation", endTime - startTime));
        TestMapPrinter.printMap(gameMap);
    }

    @Test
    void createTownHall() {
        /*
        mapGenerator.createTownHall(25, 25);
        mapGenerator.transferPlacedObjectsTilesToTileMap();
        assertEquals(gameMap.map[23][23], TileCollection.HOUSE_TILE);
        assertEquals(gameMap.map[25][27], TileCollection.HOUSE_TILE);
         */

    }

    @Test
    void createSmallHouses() {
        /*
        mapGenerator.createSmallHouses(3);
        mapGenerator.transferPlacedObjectsTilesToTileMap();
         */

    }

    @Test
    void createBigHouses() {
        /*
        mapGenerator.createBigHouses(3);
        mapGenerator.transferPlacedObjectsTilesToTileMap();
         */
    }

    @Test
    void findHouseSpots() {
    }

    @Test
    void transferHouseTilesToTileMap() {
    }
}