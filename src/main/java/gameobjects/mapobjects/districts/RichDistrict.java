package main.java.gameobjects.mapobjects.districts;

import main.java.map.District;
import main.java.map.Sector;

public class RichDistrict extends District {

    public RichDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1;
    }
}
