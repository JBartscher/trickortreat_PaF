package map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapGeneratorTest {

    private MapGenerator mapGenerator;
    private Map gameMap;

    @BeforeEach
    void setUp() {
        this.gameMap = new Map(50);
        this.mapGenerator = new MapGenerator(gameMap);
    }

    @Test
    void createMap() {
    }

    //TODO Erstmal die einzelnen Methoden
    @Test
    void testCreateMap() {
        mapGenerator.createMap();
    }

    @Test
    void createTownHall() {
        mapGenerator.createTownHall(25, 25);
        mapGenerator.transferHouseTilesToTileMap();
        assertEquals(gameMap.map[23][23], TileCollection.HOUSE_TILE);
        assertEquals(gameMap.map[25][27], TileCollection.HOUSE_TILE);

        TestMapPrinter.printMap(gameMap);
    }

    @Test
    void createSmallHouses() {
        mapGenerator.createSmallHouses(3);
        mapGenerator.transferHouseTilesToTileMap();
    }

    @Test
    void createBigHouses() {
        mapGenerator.createBigHouses(3);
        mapGenerator.transferHouseTilesToTileMap();
    }

    @Test
    void findHouseSpots() {
    }

    @Test
    void transferHouseTilesToTileMap() {
    }
}