package main.java.map;

import main.java.Configuration;
import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.*;
import main.java.gameobjects.mapobjects.districts.District;

import java.awt.*;
import java.util.Queue;
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

        generateBioms();

        // supply the center of the map
        createTownHall(gameMap.getSize() / 2 - TownHall.TOWN_HALL_HEIGHT / 2, gameMap.getSize() / 2  - TownHall.TOWN_HALL_WIDTH / 2 + 1);
        createMansion(5, 5);

        createCentreSmallHouses(5);


        createSmallHouses(((Number) config.getParam("smallHouses")).intValue());
        createBigHouses(((Number) config.getParam("bigHouses")).intValue());
        transferPlacedObjectsTilesToTileMap();
        disableHouseOffsets();
        //createStreetNetwork();

    }

    // Generiert für jeden Distrikt ein Biom
    public void generateBioms() {
        ArrayList<District> mapDistricts = districtManager.getMapDistircts();
        int mapThirdSize = gameMap.getSize() / 3;
        Random random = new Random();
        int nr = 1;
        int zahl = 1;

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {

                // Zentrum überspringen
                if(y * 3 + x == 4) continue;
                District.BiomType biomType = mapDistricts.get(y * 3 + x).getBiomType();
                for(int j = 0; j < mapThirdSize; j++) {
                    for(int i = 0; i < mapThirdSize; i++) {

                        Tile currentTile = gameMap.getMap()[y * mapThirdSize + j][x * mapThirdSize + i][0];

                        switch(biomType) {

                            case Gras:
                                zahl = random.nextInt(100);
                                if(zahl < 60) nr = 1;
                                if(zahl >= 60 && zahl < 70) nr = 2;
                                if(zahl >= 66 && zahl < 80) nr = 3;
                                if(zahl >= 80) nr = 4;
                                currentTile.setTileNr(nr);
                                break;

                            case Sand:
                                currentTile.setTileNr(5);
                                break;

                            case Desert:
                                currentTile.setTileNr(6);
                                break;

                            case Snow:
                                zahl = random.nextInt(100);
                                if(zahl < 60) nr = 7;
                                if(zahl >= 60 && zahl < 70) nr = 8;
                                if(zahl >= 66 && zahl < 80) nr = 9;
                                if(zahl >= 80) nr = 10;
                                currentTile.setTileNr(nr);
                                break;

                            default:
                                throw new IllegalStateException("Unexpected value: " + biomType);
                        }
                    }
                }
            }
        }
    }


    private void createMansion(int x, int y) {
        // 8x5
        Mansion mansion = new Mansion(x, y);
        // the first item cannot intersect with other items because its new.
        //findObjectSpot(mansion);
        gameMap.getMapSector().addMapObject(mansion);
        transferQueue.add(mansion);

        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(mansion);
            // townHall.setDistrict(districtOfHouse);
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
        }

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
            int y = r.nextInt(gameMap.getSize());
            int x = r.nextInt(gameMap.getSize());

            // TODO: Verhindert zu dichtes Spawnen am Stadtzentrum
            if(x > gameMap.getSize() * 0.36 && x < gameMap.getSize() * 0.64 && y > gameMap.getSize() * 0.36 && y < gameMap.getSize() * 0.64) continue;

            if ( (x > gameMap.getSize() * 0.27 && x < gameMap.getSize() * 0.37 ) || ( x >= gameMap.getSize() * 0.62 && x < gameMap.getSize() * 0.75)) continue;
            if ( (y > gameMap.getSize() * 0.27 && y < gameMap.getSize() * 0.37 ) || ( y >= gameMap.getSize() * 0.62 && y < gameMap.getSize() * 0.75)) continue;

            Placeable placeable = new Placeable(y, x, width, height);
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
                    gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y][1] = currentMapObject.getTileByTileIndex(x, y);
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

    private void createCentreSmallHouses(int numberOfHouses) {
        // 2x2
        int width = 2, height = 2;
        for (int i = 0; i < numberOfHouses; i++) {
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House smallHouse = new SmallHouse(0, 0, width, height);

            int x = (int) (gameMap.getSize() * 0.4) + i * 3;
            int y = (int) (gameMap.getSize() * 0.40);
            gameMap.getMapSector().addMapObject(smallHouse);
            // convey x and y pos to object
            smallHouse.setX(y);
            smallHouse.setY(x);

            transferQueue.add(smallHouse);

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


    public void createStreetNetwork() {

        ArrayList<Point> doorPoints = new ArrayList<>();
        Tile[][][] tileMap = gameMap.getMap();

        for(int y = 0; y < tileMap.length; y++){
            for(int x = 0; x < tileMap[y].length; x++) {
                int tileNr = tileMap[y][x][1].getTileNr();
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

        tileMap[y][x][1].setTileNr(21);

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

        System.out.println("Momentane Koordinaten - x : " + x + " - y: " + y);

        if(targetX > x && xAllowed) {
            if(isWalkable(x + 1, y))
                x++;

            else if(targetY < y && isWalkable(x, y - 1)){
                y--;
            }

            else if(targetY >= y && isWalkable(x, y + 1)){
                y++;
            }

            setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
            return;
        }

        else if(targetX < x && xAllowed) {

            if(isWalkable(x - 1, y))
                x--;
            else if(targetY < y && isWalkable(x, y - 1)){
                y--;
            }

            else if(targetY >= y && isWalkable(x, y + 1)){
                y++;
            }

            setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
            return;
        }

        if(targetY > y && yAllowed) {
            if(isWalkable(x, y + 1)) {
                y++;
                setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                return;
            }
            else if(isWalkable(x - 1, y)) {
                x--;
                setStreetAndRecallMethod(x, y, targetX, targetY, false, true);
                return;
            }
        }

        else if(targetY < y && yAllowed) {
            if(isWalkable(x, y - 1)) {
                y--;
                setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                return;
            }
            else if(isWalkable(x + 1, y)){
                x++;
                setStreetAndRecallMethod(x, y, targetX, targetY, false, true);
                return;
            }
        }

        if (targetX == x && targetY != y) {


            boolean ready = false;
            do {
                x--;
                setStreetAndRecallMethod(x, y, x, y, true, false);

                if (targetY > y) {
                    if (isWalkable(x, y + 1)) {
                        y++;
                        ready = true;
                        setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                    } else {

                    }
                } else if (targetY < y) {
                    if(isWalkable(x, y - 1)) {
                        y--;
                        ready = true;
                        setStreetAndRecallMethod(x, y, targetX, targetY, true, true);
                    }
                }

            } while(!ready);



        }
    }

    public boolean isWalkable(int x, int y) {
        int tileNr = gameMap.getMap()[y][x][1].getTileNr();
        return (tileNr <= 25) ? true : false;

    }

    public void setStreetAndRecallMethod(int x, int y, int targetX, int targetY, boolean xAllowed, boolean yAllowed) {
        int tileNr = gameMap.getMap()[y][x][1].getTileNr();
        int streetType = 20;
        if (tileNr < 20) {

            // TODO: FUNKTIONIERT MIT DER NEUEN KARTENGRößE NICHT MEHR
            //if (x < Map.BORDER && y < Map.BORDER) streetType = 21;
            //else if (x > Map.BORDER && y < Map.BORDER) streetType = 22;
            //else if (x < Map.BORDER && y > Map.BORDER) streetType = 23;
            //else if (x > Map.BORDER && y > Map.BORDER) streetType = 24;

            streetType = 21;

            gameMap.getMap()[y][x][1].setTileNr(streetType);
        }
            if (x != targetX || y != targetY) drawStreet(x, y, targetX, targetY, xAllowed, yAllowed);

    }

}