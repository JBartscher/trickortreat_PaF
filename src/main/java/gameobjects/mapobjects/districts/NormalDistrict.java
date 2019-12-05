package main.java.gameobjects.mapobjects.districts;

import main.java.map.District;
import main.java.map.Sector;

public class NormalDistrict extends District {

    public NormalDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 2.5;
    }
}
