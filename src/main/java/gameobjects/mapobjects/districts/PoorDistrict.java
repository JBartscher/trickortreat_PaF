package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;

public class PoorDistrict extends District {

    public PoorDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1.5;
    }
}
