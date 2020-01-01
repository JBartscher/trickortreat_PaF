package main.java.map;

import main.java.Configuration;
import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;
import main.java.gameobjects.mapobjects.TownHall;
import main.java.gameobjects.mapobjects.districts.District;

import java.awt.*;
import java.util.*;

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
        Configuration<Object> config = new Configuration<Object>();

        // supply the center of the map
        createTownHall(gameMap.getSize() / 2, gameMap.getSize() / 2);
        createSmallHouses(((Number) config.getParam("smallHouses")).intValue());
        createBigHouses(((Number) config.getParam("bigHouses")).intValue());
        transferPlacedObjectsTilesToTileMap();
        disableHouseOffsets();
        createStreetNetwork();

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


    public void createStreetNetwork() {

        ArrayList<Point> doorPoints = new ArrayList<>();
        Tile[][] tileMap = gameMap.getMap();
        for(int y = 0; y < tileMap.length; y++){
            for(int x = 0; x < tileMap[y].length; x++) {
                int tileNr = tileMap[y][x].getTileNr();

                String nr = String.valueOf(tileNr);
                if(nr.length() == 1) nr = "0" + nr;
                if(nr.length() == 3 || tileNr >= 90) nr = "XX";

                if(tileNr == 34 || tileNr == 45 || tileNr == 54 || tileNr == 65 || tileNr == 74 || tileNr == 85) {
                    nr = "DD";
                    doorPoints.add(new Point(x, y + 1));
                }

                System.out.print(nr + " ");
            }
            System.out.println();
        }


        // Start
        int size = doorPoints.size() - 1;
        int x = doorPoints.get(0).x;
        int y = doorPoints.get(0).y;
        tileMap[y][x].setTileNr(0);

        for(int i = 0; i < size; i++) {

            Point target = findLowestDistance(doorPoints, x, y);
            drawStreet(x, y, target.x, target.y, true, true);

            // remove starting point from list
            Iterator<Point> iterator = doorPoints.iterator();
            while(iterator.hasNext()) {

                Point point = iterator.next();
                if(point.x == x && point.y == y)
                {
                    iterator.remove();
                    x = target.x;
                    y = target.y;
                    break;
                }
            }

        }


    }

    public Point findLowestDistance(ArrayList<Point> doorPoints, int x, int y) {

        double min = 1000.0;
        double distance = 1000.0;
        int targetX = 0;
        int targetY = 0;
        for(Point points: doorPoints) {
            distance = Math.sqrt (  (x - points.x) * (x - points.x) + (y - points.y) * (y - points.y) ) ;
            if(distance == 0) continue;
            if(distance < min) {
                min = distance;
                targetX = points.x;
                targetY= points.y;
            }
        }

        System.out.println("Kürzeste Entfernung für x: " + x + " y: " + y + " - " + " ist " + " x: " + targetX + " - y: " + targetY);
        return new Point(targetX, targetY);

    }

    public void drawStreet(int x, int y, int targetX, int targetY, boolean xAllowed, boolean yAllowed) {


        if(targetX > x && xAllowed) {
            if(isGrasOrStreet(x + 1, y))
                x++;

            else if(targetY < y && isGrasOrStreet(x, y - 1)){
                y--;
            }

            else if(targetY > y && isGrasOrStreet(x, y + 1)){
                y++;
            }

            setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
            return;
        }

        else if(targetX < x && xAllowed) {
            if(isGrasOrStreet(x - 1, y))
                x--;
            else if(targetY < y && isGrasOrStreet(x, y - 1)){
                y--;
            }

            else if(targetY > y && isGrasOrStreet(x, y + 1)){
                y++;
            }
            setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
            return;
        }

        if(targetY > y && yAllowed) {
            if(isGrasOrStreet(x, y + 1)) {
                y++;
                setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                return;
            }
            else if(isGrasOrStreet(x + 1, y)) {
                x++;
                setStreetAndRecallMethod(x, y, targetX, targetY, false, true);
                return;
            }
        }

        else if(targetY < y && yAllowed) {
            if(isGrasOrStreet(x, y - 1)) {
                y--;
                setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                return;
            }
            else if(isGrasOrStreet(x + 1, y)){
                x++;
                setStreetAndRecallMethod(x, y, targetX, targetY, false, true);
                return;
            }

        }



        /*
        if(targetY > y) {
            if(isGrasOrStreet(x, y + 1))
                y++;
            else if (isGrasOrStreet(x - 1, y)) {
                x--;
            }
            setStreetAndRecallMethod(x, y, targetX, targetY);
        }

        else if(targetY < y) {
            if(isGrasOrStreet(x, y - 1))
                y--;
            else if(isGrasOrStreet(x - 1, y)) {
                x--;
            }
            setStreetAndRecallMethod(x, y, targetX, targetY);

        }


         */
    }

    public boolean isGrasOrStreet(int x, int y) {
        int tileNr = gameMap.getMap()[y][x].getTileNr();
        return (tileNr == 0 || tileNr == 1 || tileNr == 2 || tileNr == 3 || tileNr == 4 || tileNr == 5) ? true : false;
    }

    public void setStreetAndRecallMethod(int x, int y, int targetX, int targetY, boolean xAllowed, boolean yAllowed) {
        if(gameMap.getMap()[y][x].getTileNr() != 0)
            gameMap.getMap()[y][x].setTileNr(0);
        if(x != targetX || y != targetY) drawStreet(x, y, targetX, targetY, xAllowed, yAllowed);
    }
}