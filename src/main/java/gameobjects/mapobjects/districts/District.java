package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;

/**
 * Abstract class for Districts
 */
public abstract class District {

    protected double candy_multiplikator = 0;
    protected int houseColorKey = 5;
    private Sector sector;

    public District(Sector sector) {
        this.sector = sector;
    }

    public Sector getSector() {
        return sector;
    }

    public double getCandy_multiplikator() {
        return candy_multiplikator;
    }

    public int getHouseColorKey() {
        return houseColorKey;
    }
}
