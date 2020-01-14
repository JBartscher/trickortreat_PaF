package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

public class BigHouse extends House {
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
        /*
        if (isUnvisited) {
            
            Sound.playRing();

            // Berechne die Menge der Süßigkeiten
            int candies = 0;
            Random random = new Random();
            for(int i = 0; i < player.getChildrenCount(); i++) {
                int zahl = random.nextInt(2);
                candies += (int)(this.district.getCandy_multiplikator() + zahl);
            }

            player.addCandy(candies);
        }
        this.isUnvisited = false;
        notifyObservers(this);

         */

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
