package gameobjects.mapobjects.districts;

import map.placing_utils.Sector;

public class PoorDistrict extends District {

    public PoorDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1.5;
    }
}
