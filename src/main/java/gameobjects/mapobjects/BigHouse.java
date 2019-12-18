package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.TileCollection;

public class BigHouse extends House {
    public BigHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getBigHouseUnvisitedTiles();
    }

    @Override
    public void visit(Player player) {
        if (isUnvisited) {
            player.addCandy((int) this.district.getCandy_multiplikator() * player.getChildrenCount());
            repaintAfterVisit();
            updateMap();
        }
        System.out.println(player.getCandy());
        this.isUnvisited = false;
    }

    /**
     * This method should be called  after a house is visited, the Tileset gets changed out to one where no lights are
     * burning.
     */
    public void repaintAfterVisit() {
        this.tileset = district.getBigHouseVisitedTileset();
    }
}
