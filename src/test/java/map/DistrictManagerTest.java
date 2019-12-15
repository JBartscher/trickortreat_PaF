package map;

import main.java.exceptions.PlaceableBelongsToNoSectorException;
import main.java.exceptions.SectorOverlappingException;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.DistrictDecider;
import main.java.map.DistrictManager;
import main.java.map.Map;
import main.java.map.MapObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DistrictManagerTest {

    DistrictManager districtManager;
    Map gameMap;

    @BeforeEach
    void setUp() {
        this.gameMap = new Map(50);
        try {
            DistrictDecider districtDecider = new DistrictDecider(this.gameMap);
            this.districtManager = new DistrictManager(districtDecider.generateDistricts());
        } catch (SectorOverlappingException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }


    /**
     * Test to ensure that even an uneven map sizes create valid sectors which dont overlap each other
     */
    @Test
    void TestNoSectorsOverlapping() {
        Map gameMap_even = new Map(50);
        Map gameMap_uneven = new Map(55);

        DistrictDecider districtDecider;
        try {
            // even and valid map
            districtDecider = new DistrictDecider(gameMap_even);
            new DistrictManager(districtDecider.generateDistricts());
            // uneven but valid map
            districtDecider = new DistrictDecider(gameMap_uneven);
            new DistrictManager(districtDecider.generateDistricts());
        } catch (SectorOverlappingException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void assignDistrict() {
        MapObject p = new House(1, 1, 5, 5);
        this.districtManager.assignDistrict(p);
    }

    @Test
    void belongsToDistricts() {
        MapObject p_oneDistrict = new House(1, 1, 5, 5);
        this.districtManager.assignDistrict(p_oneDistrict);
        MapObject p_multipleDistricts = new House(0, 0, 50, 50);
        this.districtManager.assignDistrict(p_multipleDistricts);
        List<District> belongsToDistricts;
        try {
            belongsToDistricts = this.districtManager.belongsToDistricts(p_oneDistrict);
            Assertions.assertEquals(1, belongsToDistricts.size());

            belongsToDistricts = this.districtManager.belongsToDistricts(p_multipleDistricts);
            Assertions.assertEquals(4, belongsToDistricts.size());
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void getDistrict() {
        MapObject p = new House(1, 1, 5, 5);

        this.districtManager.assignDistrict(p);

        try {
            District d = this.districtManager.getDistrict(p);
            Assertions.assertEquals(0, d.getSector().getX());
            Assertions.assertEquals(0, d.getSector().getY());
            Assertions.assertEquals(25, d.getSector().getHeight());
            Assertions.assertEquals(25, d.getSector().getWidth());
        } catch (PlaceableBelongsToNoSectorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}