package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class PoorDistrict extends District {

    public PoorDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 2;
        this.houseColorKey = 7;
    }

    @Override
    public Tile[][] getSmallHouseUnvisitedTileset() {
        return TileCollection.getPoorSmallHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getSmallHouseVisitedTileset() {
        return TileCollection.getPoorSmallHouseVisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseUnvisitedTileset() {
        return TileCollection.getPoorBigHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseVisitedTileset() {
        return TileCollection.getPoorBigHouseVisitedTiles();
    }

    @Override
    public Tile[][] getGingerbreadHouseVisitedTileset(){
        return TileCollection.getGingerbreadHouseVisitedTiles();
    }

    @Override
    public Tile[][] getGingerbreadHouseUnvisitedTileset(){
        return TileCollection.getGingerbreadHouseUnvisitedTiles();
    }
}
