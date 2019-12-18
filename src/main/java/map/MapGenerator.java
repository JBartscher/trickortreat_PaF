package main.java.map;

import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;
import main.java.gameobjects.mapobjects.TownHall;
import main.java.gameobjects.mapobjects.districts.District;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {

    private static final Random r = new Random();

    DistrictManager districtManager;

    final private Map gameMap;

    /**
     * Queue of Objects that should be placed. The idea behind using a queue is that we place important Objects before
     * other, more generic Objects. This is done to cushion long running spot finding methods. After a set time the
     * Class stops finding new Spots for placebles and adds everything it got's to the gamemap.
     */
    final private Queue<MapObject> transferQueue = new LinkedList<MapObject>() {
    };

    public MapGenerator(Map map) {
        this.gameMap = map;
        DistrictDecider districtDecider = new DistrictDecider(map);
        try {
            this.districtManager = new DistrictManager(districtDecider.generateDistricts());
        } catch (SectorOverlappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which calls all the creation Objects and methods.
     * TODO: Switch with Builder Pattern.
     */
    public void createMap() {
        // supply the center of the map
        createTownHall(gameMap.getSize() / 2, gameMap.getSize() / 2);
        createSmallHouses(16);
        createBigHouses(30); //TODO: Hier bitte aus der Config
        transferPlacedObjectsTilesToTileMap();
        disableHouseOffsets();
    }

    /**
     * creates the townhall in the center of the map.
     *
     * @param x pos x on map
     * @param y pos y on map
     */
    private void createTownHall(int x, int y) {
        // 5x5
        TownHall townHall = new TownHall(x, y);
        // the first item cannot intersect with other items because its new.
        gameMap.getMapSector().addMapObject(townHall);
        transferQueue.add(townHall);
        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(townHall);
            // townHall.setDistrict(districtOfHouse);
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
        }

    }

    /**
     * tries to place at max the number of small Houses
     *
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    private void createSmallHouses(int numberOfHouses) {
        // 2x2
        int width = 2, height = 2;
        for (int i = 0; i < numberOfHouses; i++) {
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House smallHouse = new SmallHouse(0, 0, width, height);
            findObjectSpot(smallHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(smallHouse);
                System.out.println(districtOfHouse);
                smallHouse.setDistrict(districtOfHouse);
            } catch (PlaceableBelongsToNoSectorException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * tries to place at max the number of big Houses
     *
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    private void createBigHouses(int numberOfHouses) {
        // 3x3
        int width = 2, height = 3;
        for (int i = 0; i < numberOfHouses; i++) {
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House bigHouse = new BigHouse(0, 0, width, height);
            findObjectSpot(bigHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(bigHouse);
                bigHouse.setDistrict(districtOfHouse);
            } catch (PlaceableBelongsToNoSectorException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * finds a place where the given Object type can be placed.
     *
     * @param placingObject a MapObject which is more a more abstract form of every Object that should be placed on the map.
     */
    private void findObjectSpot(MapObject placingObject) {
        final long MAX_TIME_PLACING = 10; //10 ms
        long startTime = System.currentTimeMillis();

        int width = placingObject.getWidth();
        int height = placingObject.getHeight();

        while (true) {
            Placeable placeable = new Placeable(r.nextInt(gameMap.getSize()), r.nextInt(gameMap.getSize()), width, height);
            // not colliding and sector contains Placeable
            if (!gameMap.getMapSector().intersectsWithContainingItems(placeable) && gameMap.getMapSector().contains(placeable)) {
                gameMap.getMapSector().addMapObject(placingObject);
                // convey x and y pos to object
                placingObject.setX(placeable.getX());
                placingObject.setY(placeable.getY());

                transferQueue.add(placingObject);
                break;
            }
            //ensure that the loop does not run forever
            if (startTime - System.currentTimeMillis() >= MAX_TIME_PLACING)
                break;
        }
    }

    /**
     * transfers the tiles of a House object in the placing queue to the gamemap.
     */
    private void transferPlacedObjectsTilesToTileMap() {
        while (!transferQueue.isEmpty()) {
            MapObject currentMapObject = transferQueue.remove();
            int houseWidth = currentMapObject.getWidth();
            int houseHeight = currentMapObject.getHeight();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y] = currentMapObject.getTileByTileIndex(x, y);
                }
            }
        }
    }

    /**
     * disable house offset to ensure collision detection works correct
     */
    private void disableHouseOffsets() {
        gameMap.getMapSector().getAllContainingMapObjects().forEach(Placeable::disableOffset);
    }
}