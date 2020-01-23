package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

/**
 * a small house is a smaller version of a big house and represents a specialized object of the abstract class house
 */
public class SmallHouse extends House {
    public static final int SMALL_HOUSE_WIDTH = 2;
    public static final int SMALL_HOUSE_HEIGHT = 2;

    public SmallHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getNormalSmallHouseUnvisitedTiles();
    }

    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);

        this.tileset = district.getSmallHouseUnvisitedTileset();
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
        this.tileset = district.getSmallHouseVisitedTileset();
    }
}
