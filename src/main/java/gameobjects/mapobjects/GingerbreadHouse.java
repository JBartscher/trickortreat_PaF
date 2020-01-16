package main.java.gameobjects.mapobjects;

import main.java.Sound;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

public class GingerbreadHouse extends House {


    public GingerbreadHouse(int x, int y, int tileWidth, int tileHeight) {


        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getGingerbreadHouseUnvisitedTiles();
    }

    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);

        this.tileset = district.getGingerbreadHouseUnvisitedTileset();
    }

    @Override
    public void visit(Player player) {
        if(player.getProtectedTicks() > 0) return;

        if(player.hasKey() && !isUnvisited) {
            Sound.playRing();
            player.setChildrenCount(player.getChildrenCount() + 1);
            player.setHasKey(false);
            isUnvisited = true;
        } else if(!player.hasKey() || isUnvisited) {
            Sound.playRing();
            Sound.playChild();
            player.setChildrenCount(player.getChildrenCount() - 1);
            Sound.playChild();
            isUnvisited = false;
        }

        player.setProtectedTicks(50);
        notifyObservers(observers);
    }

    @Override
    public void repaintAfterVisit() {

            if(!isUnvisited) {
                this.tileset = district.getGingerbreadHouseVisitedTileset();
            } else {
                this.tileset = district.getGingerbreadHouseUnvisitedTileset();
            }
        }
        
}



