package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

/**
 * represents a real house object with a bigger size than a small house
 */
public class BigHouse extends House {

    public static final int BIG_HOUSE_WIDTH = 2;
    public static final int BIG_HOUSE_HEIGHT = 3;

    public BigHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getNormalBigHouseUnvisitedTiles();
    }

    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);
        this.tileset = district.getBigHouseUnvisitedTileset();
    }

    @Override
    public void visit(Player player) {
        super.visit(player);
    }

    /**
     * This method should be called  after a house is visited, the Tileset gets changed out to one where no lights are
     * burning.
     */
    public void repaintAfterVisit() {
        this.tileset = district.getBigHouseVisitedTileset();
    }
}
