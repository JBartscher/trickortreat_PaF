package gameobjects.mapobjects.districts;

import map.placing_utils.Sector;

public class RichDistrict extends District {

    public RichDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1;
    }
}
