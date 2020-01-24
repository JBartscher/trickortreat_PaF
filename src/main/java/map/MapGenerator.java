package main.java.map;

import main.java.Configuration;
import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.*;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.gameobjects.mapobjects.districts.NormalDistrict;
import main.java.pathfinding.AStar;
import main.java.pathfinding.Node;

import java.awt.*;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * this class is responsible to generate the game map and biomes
 */
public class MapGenerator {

    private static final Random r = new Random();
    private static final Configuration<Object> config = new Configuration<Object>();

    DistrictManager districtManager;

    final private Map gameMap;
    private AStar aStar;

    private boolean witchHouseSet = false;

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
            fillMap(); // A Star needs a initialized map
            this.aStar = new AStar(gameMap);
        } catch (SectorOverlappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which calls all the creation Objects and methods.
     * TODO: Switch with Builder Pattern.
     */
    public void createMap() {

        generateBioms();

        // supply the center of the map
        createTownHall(gameMap.getSize() / 2 - TownHall.TOWN_HALL_HEIGHT / 2, gameMap.getSize() / 2 - TownHall.TOWN_HALL_WIDTH / 2 + 1);
        createMansion(4, 3); //TODO: remove fixed value (@see collideWithAliceCooper() in MovementManager)

        createCentreSmallHouses(4);
        createCentreBigHouses(8);
        createCentreHouses();

        createHouses();
        transferPlacedObjectsTilesToTileMap();
        disableHouseOffsets();
        // TODO createStreetNetwork();
    }

    /**
     *
     */
    private void fillMap() {
        /**
         * iterate over the whole map and create a tile
         */

        for (int y = 0; y < gameMap.getSize(); y++) {
            for (int x = 0; x < gameMap.getSize(); x++) {
                fillTile(y, x);
            }
        }

    }

    /**
     * create each tile on the given coordinates
     */
    private void fillTile(int y, int x) {
        // preset the tile with 0
        gameMap.map[y][x] = new Tile(0);

        /**
         * embellish the centre with obstacles and decorations
         */
        if (x > Map.xTopLeftCentre.x && x < Map.xTopRightCentre.x && y > Map.xTopLeftCentre.y && y < Map.xBottomLeftCentre.y) {
            // TODO Relevant?
            gameMap.map[y][x] = new Tile(0);

            gameMap.map[y][x] = new Tile(325);

            if (x % 3 == 0 && (y + x) % 5 == 0) {
                //gameMap.map[y][x][1] = new Tile(-7);
                gameMap.map[y][x].objectLayer.setImage(-7);

            } else if (x % 6 == 0 && (y + x) % 6 == 0) {
                //gameMap.map[y][x][1] = new Tile(-307);
                gameMap.map[y][x].objectLayer.setImage(-307);

            } else if (x % 7 == 0 && (y + x) % 7 == 0) {
                //gameMap.map[y][x][1] = new Tile(-308);
                gameMap.map[y][x].objectLayer.setImage(-308);
            } else if (x % 8 == 0 && (y + x) % 8 == 0) {
                //gameMap.map[y][x][1] = new Tile(-309);
                gameMap.map[y][x].objectLayer.setImage(-309);
            }

            if (x % 4 == 0 && (y + x) % 3 == 0) {
                //gameMap.map[y][x][1] = new Tile(26);
                gameMap.map[y][x].objectLayer.setImage(26);
            }

            if ((x % 4 == 0 && (y + x) % 2 == 0) || (x % 3 == 0 && (y - x) % 2 == 0)) {
                gameMap.map[y][x] = new Tile(327);
            }


            if (y == 33 && x == 29) {
                gameMap.map[y][x].objectLayer.setImage(-311); // = new Tile(-311);
                gameMap.map[y][x] = new Tile(326);
            }

            if (y == 31 && x == 27) {
                // gameMap.map[y][x][1] = new Tile(-320);
                gameMap.map[y][x].objectLayer.setImage(-320); // = new Tile(-311);
                gameMap.map[y][x] = new Tile(327);
            }

            if (y == 33 && x == 33) {
                // gameMap.map[y][x][1] = new Tile(-311);
                gameMap.map[y][x].objectLayer.setImage(-311); // = new Tile(-311);
                gameMap.map[y][x] = new Tile(326);
            }

            if (y == 31 && x == 35) {
                // gameMap.map[y][x][1] = new Tile(-320);
                gameMap.map[y][x].objectLayer.setImage(-320); // = new Tile(-311);
                gameMap.map[y][x] = new Tile(327);
            }

            // fountain
            if (y == 34 && x == 31) {
                gameMap.map[y][x] = new Tile(-306);
            }
            return;
        }

        /**
         * generate borders between biomes and districts
         */
        if ((x == gameMap.getSize() / 3 || x == (gameMap.getSize() / 3 + 1) || y == gameMap.getSize() / 3 || y == (gameMap.getSize() / 3 + 1)) || (x == gameMap.getSize() * 2 / 3 || x == (gameMap.getSize() * 2 / 3 + 1) || y == gameMap.getSize() * 2 / 3 || y == (gameMap.getSize() * 2 / 3 + 1))) {
            gameMap.map[y][x].objectLayer.setImage(20); // = new Tile(20);
            gameMap.map[y][x] = new Tile(20);
            return;
        }
    }

    /**
     * generates biomes for every district
     */
    public void generateBioms() {
        ArrayList<District> mapDistricts = districtManager.getMapDistricts();
        int mapThirdSize = gameMap.getSize() / 3;
        Random random = new Random();
        int nr = 1;
        int zahl = 1;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {

                // Zentrum überspringen
                if (y * 3 + x == 4) continue;
                District.BiomeType biomType = mapDistricts.get(y * 3 + x).getBiomeType();
                for (int j = 0; j < mapThirdSize; j++) {
                    for (int i = 0; i < mapThirdSize; i++) {

                        int yTotal = y * mapThirdSize + j;
                        int xTotal = x * mapThirdSize + i;

                        Tile currentTile = gameMap.getMap()[yTotal][xTotal];
                        Tile currentTileDeko = new Tile(gameMap.getMap()[yTotal][xTotal].getTileNr());

                        switch (biomType) {

                            case Gras:
                                zahl = random.nextInt(100);
                                if (zahl < 60) {
                                    nr = 1;
                                    if (zahl < 15)
                                        if (buildableWithDeko(yTotal, xTotal)) {
                                            if (zahl < 5) {
                                                //gameMap.getMap()[yTotal][xTotal][1] = new Tile(-7);
                                                gameMap.getMap()[yTotal][xTotal].objectLayer.setImage(-7);
                                            } else
                                                //gameMap.getMap()[yTotal][xTotal][1] = new Tile(-5);
                                                gameMap.getMap()[yTotal][xTotal].objectLayer.setImage(-5);

                                        }

                                }
                                if (zahl >= 60 && zahl < 70) nr = 2;
                                if (zahl >= 66 && zahl < 80) nr = 3;
                                if (zahl >= 80) nr = 4;
                                gameMap.getMap()[yTotal][xTotal] = new Tile(nr);
                                gameMap.getMap()[yTotal][xTotal].baseLayer.setImage(nr);


                                break;

                            case Sand:
                                zahl = random.nextInt(100);
                                if (zahl < 12)
                                    if (buildableWithDeko(yTotal, xTotal)) {
                                        if (zahl < 8) currentTileDeko = new Tile(-3);
                                        else
                                            currentTileDeko = new Tile(-7);
                                    }

                                if (zahl < 60) nr = 5;
                                if (zahl >= 60 && zahl < 70) nr = 6;
                                if (zahl >= 66 && zahl < 80) nr = 7;
                                if (zahl >= 80) nr = 8;

                                currentTile = new Tile(nr);
                                gameMap.getMap()[yTotal][xTotal] = currentTile;
                                gameMap.getMap()[yTotal][xTotal].baseLayer.setImage(currentTile.getTileNr());
                                gameMap.getMap()[yTotal][xTotal] = currentTileDeko;
                                gameMap.getMap()[yTotal][xTotal].objectLayer.setImage(currentTileDeko.getTileNr());
                                break;

                            case Desert:
                                currentTile = new Tile(9);
                                zahl = random.nextInt(100);
                                if (zahl < 10) {
                                    if (buildableWithDeko(yTotal, xTotal)) {
                                        if (zahl < 5)
                                            currentTileDeko = new Tile(-12);
                                        else
                                            currentTileDeko = new Tile(-13);
                                    }

                                } else if (zahl >= 12 && zahl < 20) {
                                    currentTile = new Tile(10);
                                } else if (zahl >= 20 && zahl < 26 && currentTile.getTileNr() < 20 && currentTile.getTileNr() > 25)
                                    currentTileDeko = new Tile(11);

                                gameMap.getMap()[yTotal][xTotal] = currentTile;
                                gameMap.getMap()[yTotal][xTotal].baseLayer.setImage(currentTile.getTileNr());
                                gameMap.getMap()[yTotal][xTotal] = currentTileDeko;
                                gameMap.getMap()[yTotal][xTotal].objectLayer.setImage(currentTileDeko.getTileNr());
                                break;
                            case Snow:
                                zahl = random.nextInt(100);
                                if (zahl < 15) nr = 15;
                                if (zahl >= 15 && zahl < 28) nr = 16;
                                if (zahl >= 28 && zahl < 35) nr = 17;
                                if (zahl >= 35 && zahl < 40) nr = 18;
                                if (zahl >= 40 && zahl < 45) nr = -19;

                                /**
                                 * sets snow as ground if underlying ground is NOT a street tile
                                 */
                                if (currentTile.getTileNr() < 20 && currentTile.getTileNr() > 25) {
                                    gameMap.getMap()[yTotal][xTotal] = new Tile(14);
                                    gameMap.getMap()[yTotal][xTotal].baseLayer.setImage(14);

                                }

                                if ((currentTile.getTileNr() < 20 || currentTile.getTileNr() > 25) && zahl < 60) {
                                    gameMap.getMap()[yTotal][xTotal] = new Tile(nr);
                                    gameMap.getMap()[yTotal][xTotal].baseLayer.setImage(nr);
                                }

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
        gameMap.getMapSector().addMapObject(mansion);
        transferQueue.add(mansion);

        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(mansion);
            mansion.setDistrict(districtOfHouse);
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
        TownHall townHall = new TownHall(x, y);
        // the first item cannot intersect with other items because its new.
        gameMap.getMapSector().addMapObject(townHall);
        transferQueue.add(townHall);
        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(townHall);
            townHall.setDistrict(districtOfHouse);
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
        }
    }


    /**
     * reads amount of houses from configs and creates houses with the help of a House-Factory.
     * The first bigHouse that spawns in a normal district gets replaced by the gingerBreadHouse of the witch.
     */
    private void createHouses() {
        ArrayList<String> amountOfHouses = new ArrayList<>();

        for (int i = 0; i < ((Number) config.getParam("smallHouses")).intValue(); i++) {
            amountOfHouses.add("small");
        }

        for (int i = 0; i < ((Number) config.getParam("bigHouses")).intValue(); i++) {
            amountOfHouses.add("big");
        }

        /**
         * create HouseObject for every Item in ArrayList with HouseFactory
         * find free location and assign house to a district
         */
        for (String type : amountOfHouses) {
            House house = HouseFactory.createNewInstance(type);
            findObjectSpot(house);

            // put the right district to the house object
            try {

                District districtOfHouse = districtManager.getDistrict(house);
                /**
                 * No witchHouse set yet, house is a bigHouse in a NormalDistrict
                 */
                if (!witchHouseSet && house instanceof BigHouse && districtOfHouse instanceof NormalDistrict) {
                    System.out.println("Witchhousespot in: " + districtOfHouse + " y: " + house.getX() + " x: " + house.getY());
                    /**
                     * remove all traces of the old bigHouse-object in game relevant collections
                     */
                    gameMap.getMapSector().removeMapObject(house);
                    transferQueue.remove(house);
                    /**
                     * replace the old House with a gingerBreadHouse-object
                     */
                    GingerbreadHouse gingerbreadHouse = GingerbreadHouse.getInstance();
                    gingerbreadHouse.setX(house.getX());
                    gingerbreadHouse.setY(house.getY());
                    // assign to sector, transferQueue and ensure that no other witchhouse is placed
                    gameMap.getMapSector().addMapObject(gingerbreadHouse);
                    transferQueue.add(gingerbreadHouse);
                    witchHouseSet = true;
                    gingerbreadHouse.setDistrict(districtOfHouse);
                } else {
                    gameMap.getMapSector().addMapObject(house);
                    transferQueue.add(house);
                    house.setDistrict(districtOfHouse);
                }
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
            int y = r.nextInt(gameMap.getSize() - 4) + 2;
            int x = r.nextInt(gameMap.getSize() - 4) + 2;

            // TODO: Verhindert zu dichtes Spawnen am Stadtzentrum
            if (x > gameMap.getSize() * 0.36 && x < gameMap.getSize() * 0.64 && y > gameMap.getSize() * 0.36 && y < gameMap.getSize() * 0.64)
                continue;

            if ((x > gameMap.getSize() * 0.27 && x < gameMap.getSize() * 0.37) || (x >= gameMap.getSize() * 0.62 && x < gameMap.getSize() * 0.75))
                continue;
            if ((y > gameMap.getSize() * 0.27 && y < gameMap.getSize() * 0.37) || (y >= gameMap.getSize() * 0.62 && y < gameMap.getSize() * 0.75))
                continue;

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
                    if (x == 0 && currentMapObject instanceof House) {
                        //gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y][2] = currentMapObject.getTileByTileIndex(x, y);
                        gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y].coverLayer.setImage(currentMapObject.getTileByTileIndex(x, y).getTileNr());
                        //gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y][1] = currentMapObject.getTileByTileIndex(x, y);
                        gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y].objectLayer.setImage(currentMapObject.getTileByTileIndex(x, y).getTileNr());
                    } else
                        //gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y][1] = currentMapObject.getTileByTileIndex(x, y);
                        gameMap.map[currentMapObject.getX() + x][currentMapObject.getY() + y].objectLayer.setImage(currentMapObject.getTileByTileIndex(x, y).getTileNr());
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

    /**
     * places a number of houses in the centre of the map.
     *
     * @param numberOfHouses the amount of houses that get placed at the center of the map
     */
    private void createCentreSmallHouses(int numberOfHouses) {
        // 2x2
        for (int i = 0; i < numberOfHouses; i++) {
            // stub Object, the placeable will be overridden in the findObjectSpot method
            House smallHouse = HouseFactory.createNewInstance("small");

            int x = (int) (gameMap.getSize() * 0.42) + i * 3;
            int y = (int) (gameMap.getSize() * 0.38);
            addAndSetHouse(smallHouse, x, y);
        }
    }

    /**
     * places a number of houses in the centre of the map.
     *
     * @param numberOfHouses the amount of houses that get placed at the center of the map
     */
    private void createCentreBigHouses(int numberOfHouses) {
        // 2x2
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < numberOfHouses / 2; i++) {
                // stub Object, the placeable will be overridden in the findObjectSpot method
                House bigHouse = HouseFactory.createNewInstance("big");

                int x = (int) (gameMap.getSize() * 0.39) + i * 4;
                int y = (int) (gameMap.getSize() * 0.42);

                if (j == 1) {
                    y += 11;
                }
                /**
                 * set house and add it to sector
                 */
                addAndSetHouse(bigHouse, x, y);
            }
        }
    }

    /**
     * create houses in the centre of the map
     */
    private void createCentreHouses() {

        House house = HouseFactory.createNewInstance("big");
        int x = 23;
        int y = 30;
        addAndSetHouse(house, x, y);

        x = 36;
        y = 30;
        house = HouseFactory.createNewInstance("big");
        addAndSetHouse(house, x, y);


    }

    /**
     * set the house to given coordinates and add it to the sector
     *
     * @param house
     * @param x
     * @param y
     */
    private void addAndSetHouse(House house, int x, int y) {
        gameMap.getMapSector().addMapObject(house);
        // convey x and y pos to object
        house.setX(y);
        house.setY(x);

        transferQueue.add(house);

        // put the right district to the house object
        try {
            District districtOfHouse = districtManager.getDistrict(house);
            System.out.println(districtOfHouse);
            house.setDistrict(districtOfHouse);
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
        }

    }


    /**
     * create the street network with AStar algorithm
     */
    public void createStreetNetwork() {
        ArrayList<Point> doorPoints = new ArrayList<>();
        Tile[][] tileMap = gameMap.getMap();

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                int tileNr = tileMap[y][x].objectLayer.getImageNr();
                String nr = String.valueOf(tileNr);
                if (nr.length() == 1) nr = "0" + nr;
                if (nr.length() == 3 || tileNr >= 90) nr = "XX";

                if (tileNr == 34 || tileNr == 45 || tileNr == 54 || tileNr == 65 || tileNr == 70 || tileNr == 74 || tileNr == 85 || tileNr == 110 || tileNr == 224) {

                    //nr = "DD";
                    doorPoints.add(new Point(x, y + 1));
                }
            }
        }

        // Start
        int size = doorPoints.size() - 1;
        int x = doorPoints.get(0).x;
        int y = doorPoints.get(0).y;

        tileMap[y][x].objectLayer.setImage(getBiomeStreetType(x, y)); // = new Tile(getBiomeStreetType(x, y));

        for (int i = 0; i < size; i++) {

            Point target = findLowestDistance(doorPoints, x, y);
            findStreetConnection(x, y, target.x, target.y);

            // remove starting point from list
            Iterator<Point> iterator = doorPoints.iterator();
            while (iterator.hasNext()) {

                Point point = iterator.next();
                iterator.remove();
                x = point.x;
                y = point.y;
                break;
            }
        }
    }

    // uses AStar Algorithm
    public void findStreetConnection(int x, int y, int targetX, int targetY) {

        aStar.setStartPosition(new Point(x, y));
        aStar.setTargetPosition(new Point(targetX, targetY));

        CopyOnWriteArrayList<Point> targets = new CopyOnWriteArrayList<>();
        targets.clear();
        aStar.fillMapForStreetNetwork(gameMap.getMap(), true);
        ArrayList<Node> nodes = aStar.executeAStar();

        if (nodes != null) {
            for (Node node : nodes) {
                targets.add(node.getPosition());
            }
            targets.add(new Point(targetX, targetY));

            drawStreet(targets);
        } else {
            System.out.println("KEIN PFAD GEFUNDEN!");
        }
    }

    /**
     * check if current tile is replaceable by a decoration tile
     *
     * @param yTotal
     * @param xTotal
     * @return
     */
    public boolean buildableWithDeko(int yTotal, int xTotal) {

        //if ((xTotal <= 1 && yTotal <= 1) || (xTotal >= 58 && yTotal >= 58)) return false;

        return gameMap.getMap()[yTotal][xTotal].objectLayer.getImageNr() == 0;
    }

    /**
     * find the next nearest door
     *
     * @param doorPoints
     * @param x
     * @param y
     * @return
     */
    public Point findLowestDistance(ArrayList<Point> doorPoints, int x, int y) {

        double min = 1000.0;
        double distance = 1000.0;
        int targetX = 0;
        int targetY = 0;
        for (Point points : doorPoints) {
            distance = Math.sqrt((x - points.x) * (x - points.x) + (y - points.y) * (y - points.y));
            if (distance == 0) continue;
            if (distance < min) {
                min = distance;
                targetX = points.x;
                targetY = points.y;
            }
        }
        //System.out.println("Kürzeste Entfernung für x: " + x + " y: " + y + " - " + " ist " + " x: " + targetX + " - y: " + targetY);
        return new Point(targetX, targetY);
    }

    /**
     * draw street tiles depending on given targets
     *
     * @param targets
     */
    public void drawStreet(CopyOnWriteArrayList<Point> targets) {

        for (Point point : targets) {
            int tileNr = gameMap.getMap()[point.y][point.x].objectLayer.getImageNr();
            int streetType = 20;
            if (tileNr < 20 || tileNr == 26) {

                streetType = getBiomeStreetType(point.x, point.y);

                gameMap.getMap()[point.y][point.x].objectLayer.setImage(streetType);
            }
        }
    }

    /**
     * return the biome depending on x- and y-variable
     *
     * @param x
     * @param y
     * @return
     */
    public int getBiomeStreetType(int x, int y) {

        if (x > Map.xTopLeftCentre.x && x < Map.xTopRightCentre.x && y > Map.xTopLeftCentre.y && y < Map.xBottomLeftCentre.y) {
            return 25;
        }

        int sectorNumbY = y / 20;
        int sectorNumbX = x / 20;

        District dis = districtManager.getMapDistricts().get(sectorNumbY * 3 + sectorNumbX);

        switch (dis.getBiomeType()) {

            case Gras:
                return 21;
            case Sand:
                return 22;
            case Desert:
                return 23;
            case Snow:
                return 24;
        }

        return 21;

    }
}