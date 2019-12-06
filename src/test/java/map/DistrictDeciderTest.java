package map;

import main.java.map.District;
import main.java.map.DistrictDecider;
import main.java.map.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DistrictDeciderTest {

    @Test
    void generateDistricts() {
        Map mockMap = new Map(20);
        DistrictDecider districtDecider = new DistrictDecider(mockMap);
        List<District> districts = districtDecider.generateDistricts();
        // Needs to have exact 4 elements
        Assertions.assertEquals(4, districts.size());
        // test that all districts are created
        districts.forEach(district -> Assertions.assertNotNull(district));
    }
}