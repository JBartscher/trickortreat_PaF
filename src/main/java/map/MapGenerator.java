package map;

import exceptions.PlacebleBelongsToNoSectorException;
import exceptions.SectorOverlappingException;
import gameobjects.mapobjects.House;
import gameobjects.mapobjects.TownHall;
import gameobjects.mapobjects.districts.District;
import map.placing_utils.Placeble;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class MapGenerator {

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

    MapGenerator(Map map) {
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
        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(townHall.placeble);
            townHall.setDistrict(districtOfHouse);
        } catch (PlacebleBelongsToNoSectorException e) {
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
            // stub Object, the placeble will be overridden in the findObjectSpot method
            House smallHouse = new House(new Placeble(0, 0, width, height));
            findObjectSpot(smallHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(smallHouse.placeble);
                // System.out.println(districtOfHouse);
                smallHouse.setDistrict(districtOfHouse);
            } catch (PlacebleBelongsToNoSectorException e) {
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
            // stub Object, the placeble will be overridden in the findObjectSpot method
            House bigHouse = new House(new Placeble(0, 0, width, height));
            findObjectSpot(bigHouse);
            // put the right district to the house object
            try {
                District districtOfHouse = districtManager.getDistrict(bigHouse.placeble);
                bigHouse.setDistrict(districtOfHouse);
            } catch (PlacebleBelongsToNoSectorException e) {
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

        int width = placingObject.getPlaceble().getWidth();
        int height = placingObject.getPlaceble().getHeight();

        while (true) {
            Placeble placeble = new Placeble(r.nextInt(gameMap.getSize_x()), r.nextInt(gameMap.getSize_y()), width, height);
            if (!gameMap.getMapSector().intersectsWithContainingItems(placeble) && gameMap.getMapSector().contains(placeble)) {
                gameMap.getMapSector().addPlaceable(placeble);
                placingObject.setPlaceble(placeble);

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
            int houseWidth = currentMapObject.getPlaceble().getWidth();
            int houseHeight = currentMapObject.getPlaceble().getHeight();
            for (int x = 0; x < houseWidth; x++) {
                for (int y = 0; y < houseHeight; y++) {
                    gameMap.map[currentMapObject.getPlaceble().getX() + x][currentMapObject.getPlaceble().getY() + y] = currentMapObject.getTileByTileIndex(x, y);
                }
            }
        }
    }
}
