package map;

import gameobjects.House;
import gameobjects.TownHall;
import map.placing_utils.Placeble;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class MapGenerator {

    private static Random r = new Random();

    private Map gameMap;

    /**
     * Queue of Objects that should be placed. The idea behind using a queue is that we place important Objects before
     * other, more generic Objects. This is done to cushion long running spot finding methods. After a set time the
     * Class stops finding new Spots for placebles and adds everything it got's to the gamemap.
     */
    private Queue<MapObject> transferQueue = new LinkedList<MapObject>() {
    };

    MapGenerator(Map map) {
        this.gameMap = map;
    }

    /**
     * Method which calls all the creation Objects and methods.
     * TODO: Switch with Builder Pattern.
     */
    void createMap() {
        // supply the center of the map
        createTownHall(gameMap.getSize_x() / 2, gameMap.getSize_y() / 2);
        createSmallHouses(16);
        createBigHouses(30);
        transferPlacedObjectsTilesToTileMap();
    }

    /**
     * creates the townhall in the center of the map.
     *
     * @param x pos x on map
     * @param y pos y on map
     */
    void createTownHall(int x, int y) {
        // 5x5
        TownHall townHall = new TownHall(x, y);
        // the first item cannot intersect with other items because its new.
        gameMap.getMapSector().addPlaceable(townHall.getPlaceble());
        transferQueue.add(townHall);
    }

    /**
     * tries to place at max the number of small Houses
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    void createSmallHouses(int numberOfHouses) {
        // 2x2
        int width = 2, height = 2;
        for (int i = 0; i < numberOfHouses; i++) {
            findObjectSpot(width, height);
        }
    }

    /**
     * tries to place at max the number of big Houses
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    void createBigHouses(int numberOfHouses) {
        // 3x3
        int width = 3, height = 3;
        for (int i = 0; i < numberOfHouses; i++) {
            findObjectSpot(width, height);
        }
    }

    /**
     * finds a place where the given Object type can be placed.
     *
     * @param width width of the Objecttype that should be placed
     * @param height height of the Objecttype that should be placed
     */
    private void findObjectSpot(int width, int height) {
        final long MAX_TIME_PLACING = 10; //10 ms
        long startTime = System.currentTimeMillis();
        while (true) {
            Placeble placeble = new Placeble(r.nextInt(gameMap.getSize_x()), r.nextInt(gameMap.getSize_y()), width, height);
            if (!gameMap.getMapSector().intersectsWithContainingItems(placeble) && gameMap.getMapSector().contains(placeble)) {
                gameMap.getMapSector().addPlaceable(placeble);
                House bigHouse = new House(placeble);
                transferQueue.add(bigHouse);
                break;
            }
            //ensure that the loop does not run forever
            if (startTime - System.currentTimeMillis() >= MAX_TIME_PLACING)
                break;
        }
    }

    /**
     * This method calculates how much place on the gamemap is left.
     * @param tilecount
     * @return
     */
    public int calculateFreeTilesCountAfterPlacing(int tilecount){
        //TODO implement
        return 0;
    }

    /**
     * transfers the tiles of a House object in the placing queue to the gamemap.
     */
    void transferPlacedObjectsTilesToTileMap() {
        while (!transferQueue.isEmpty()) {
            MapObject currentMapObject = transferQueue.remove();
            int houseWidth = currentMapObject.getPlaceble().getWidth(), houseHeight = currentMapObject.getPlaceble().getWidth();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentMapObject.getPlaceble().getX() + x][currentMapObject.getPlaceble().getY() + y] = currentMapObject.getTileByTileIndex(x, y);
                }
            }
        }
    }
}
