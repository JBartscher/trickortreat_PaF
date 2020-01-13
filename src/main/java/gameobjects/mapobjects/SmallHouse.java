package main.java.gameobjects.mapobjects;

import main.java.Sound;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

import java.util.Random;

public class SmallHouse extends House {
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
        if (isUnvisited) {
            try {
                Sound.playRing();
            } catch (NoClassDefFoundError ex) {
                ex.printStackTrace();
            }

            // Berechne die Menge der Süßigkeiten
            int candies = 0;
            Random random = new Random();
            int zahl = random.nextInt(2);
            for(int i = 0; i < player.getChildrenCount(); i++) {
                candies += (int)(this.district.getCandy_multiplikator() + zahl);
            }
            System.out.println(candies);
            player.addCandy(candies);

            notifyObservers(this);
        }

        this.isUnvisited = false;
    }

    /**
     * This method should be called  after a house is visited, the Tileset gets changed out to one where no lights are
     * burning.
     */
    public void repaintAfterVisit() {
        this.tileset = district.getSmallHouseVisitedTileset();
    }
}
