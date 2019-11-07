package map;

import gameobjects.House;
import map.placing_utils.Placeble;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class MapGenerator {

    private static Random r = new Random();

    private Map gameMap;


    private Queue<House> transferQueue = new LinkedList<House>() {
    };

    MapGenerator(Map map) {
        this.gameMap = map;
    }

    void createMap() {
        // supply the center of the map
        createTownHall(gameMap.getSize_x() / 2, gameMap.getSize_y() / 2);
        createSmallHouses(4);
        createBigHouses(3);
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


    void createSmallHouses(int numberOfHouses) {
        // 2x2
        int width = 2, height = 2;
        findHouseSpots(numberOfHouses, width, height);
    }


    void createBigHouses(int numberOfHouses) {
        // 3x3
        int width = 3, height = 3;
        findHouseSpots(numberOfHouses, width, height);
    }

    private void findHouseSpots(int numberOfHouses, int width, int height) {
        for (int h = 0; h < numberOfHouses; h++) {
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
    }

    void transferHouseTilesToTileMap() {
        while (!transferQueue.isEmpty()) {
            House currentHouse = transferQueue.remove();
            int houseWidth = currentHouse.getPlaceble().getWidth(), houseHeight = currentHouse.getPlaceble().getWidth();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentHouse.getPlaceble().getX() + x][currentHouse.getPlaceble().getY() + y] = currentHouse.getHouseTile(x, y);
                }
            }
        }
    }
}
