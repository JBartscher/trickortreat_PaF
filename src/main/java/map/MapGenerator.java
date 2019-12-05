package main.java.map;

import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.TownHall;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {

    private static Random r = new Random();

    DistrictManager districtManager;

    private Map gameMap;

    /**
     * Queue of Objects that should be placed. The idea behind using a queue is that we place important Objects before
     * other, more generic Objects. This is done to cushion long running spot finding methods. After a set time the
     * Class stops finding new Spots for placebles and adds everything it got's to the gamemap.
     */
    private Queue<MapObject> transferQueue = new LinkedList<MapObject>() {
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
        gameMap.getMapSector().addPlaceable(townHall.getPlaceable());
        transferQueue.add(townHall);
        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(townHall.getPlaceable());
            townHall.setDistrict(districtOfHouse);
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
        }

    }

    /**
     * tries to place at max the number of small Houses
     * @param numberOfHouses number of houses that will be placed to the map at max
     */
    void createSmallHouses(int numberOfHouses) {
        // 2x2
        int width = 2, height = 2;
        for (int i = 0; i < numberOfHouses; i++) {
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House smallHouse = new House(new Placeable(0, 0, width, height));
            findObjectSpot(smallHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(smallHouse.getPlaceable());
                // WORKS System.out.println(districtOfHouse);
                smallHouse.setDistrict(districtOfHouse);
            } catch (PlaceableBelongsToNoSectorException e) {
                e.printStackTrace();
            }
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
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House bigHouse = new House(new Placeable(0, 0, width, height));
            findObjectSpot(bigHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(bigHouse.getPlaceable());
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

        int width = placingObject.getPlaceable().getWidth();
        int height = placingObject.getPlaceable().getHeight();

        while (true) {
            Placeable placeable = new Placeable(r.nextInt(gameMap.getSize_x()), r.nextInt(gameMap.getSize_y()), width, height);
            if (!gameMap.getMapSector().intersectsWithContainingItems(placeable) && gameMap.getMapSector().contains(placeable)) {
                gameMap.getMapSector().addPlaceable(placeable);
                placingObject.setPlaceable(placeable);

                transferQueue.add(placingObject);
                break;
            }
            //ensure that the loop does not run forever
            if (startTime - System.currentTimeMillis() >= MAX_TIME_PLACING)
                break;
        }
    }


    /**
     * This method calculates how much place on the gamemap is left.
     * Convenience method, not yet implemented.
     *
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
            int houseWidth = currentMapObject.getPlaceable().getWidth();
            int houseHeight = currentMapObject.getPlaceable().getHeight();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentMapObject.getPlaceable().getX() + x][currentMapObject.getPlaceable().getY() + y] = currentMapObject.getTileByTileIndex(x, y);
                }
            }
        }
    }
}