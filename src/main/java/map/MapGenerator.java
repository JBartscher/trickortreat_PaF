package map;

import gameobjects.House;
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
    private Queue<House> transferQueue = new LinkedList<House>() {
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
        transferHouseTilesToTileMap();
    }

    /**
     * creates the townhall in the center of the map.
     *
     * @param x pos x on map
     * @param y pos y on map
     */
    void createTownHall(int x, int y) {
        // 5x5
        int width = 5, height = 5;
        House townHall = new House(x - (width / 2), y - (height / 2), width, height);
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
        findObjectSpots(numberOfHouses, width, height);
    }

    /**
     * tries to place at max the number of big Houses
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    void createBigHouses(int numberOfHouses) {
        // 3x3
        int width = 3, height = 3;
        findObjectSpots(numberOfHouses, width, height);
    }

    /**
     * finds a place where the given Objecttype can be placed.
     *
     * TODO: implement a way to use generic Objects and assign a special Object type in a following method.
     *
     * @param numberOfObjects
     * @param width width of the Objecttype that should be placed
     * @param height height of the Objecttype that should be placed
     */
    private void findObjectSpots(int numberOfObjects, int width, int height) {
        long startTime = System.currentTimeMillis();
        for (int h = 0; h < numberOfObjects; h++) {
            //TODO: potenzielle Endlosschleife
            while (true) {
                Placeble placeble = new Placeble(r.nextInt(gameMap.getSize_x()), r.nextInt(gameMap.getSize_y()), width, height);
                if (!gameMap.getMapSector().intersectsWithContainingItems(placeble) && gameMap.getMapSector().contains(placeble)) {
                    gameMap.getMapSector().addPlaceable(placeble);
                    House bigHouse = new House(placeble);
                    transferQueue.add(bigHouse);
                    break;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("time needed for %s objects: %s",numberOfObjects, endTime-startTime));
    }

    /**
     * This method calculates how much place on the gamemap is left.
     * @param tilecount
     * @return
     */
    public int calculateFreeTilesCountAfterPlacing(int tilecount){
        return 0;
    }

    /**
     * transfers the tiles of a House object in the placing queue to the gamemap.
     */
    void transferHouseTilesToTileMap() {
        while (!transferQueue.isEmpty()) {
            House currentHouse = transferQueue.remove();
            int houseWidth = currentHouse.getPlaceble().getWidth(), houseHeight = currentHouse.getPlaceble().getWidth();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentHouse.getPlaceble().getX() + x][currentHouse.getPlaceble().getY() + y] = currentHouse.getTileByTileIndex(x, y);
                }
            }
        }
    }
}
