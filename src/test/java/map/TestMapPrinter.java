package map;

import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.Map;
import main.java.map.Sector;

import java.util.List;

public class TestMapPrinter {

    static void printMap(Map map) {
        for (int x = 0; x < map.getMap()[0].length; x++) {
            for (int y = 0; y < map.getMap().length; y++) {
                // System.out.print(map.getMap()[x][y].getTileNr());
            }
            System.out.print("\n");
        }
    }

    static void printDistrictSectors(List<District> districts){
        for (District district: districts) {
            Sector s = district.getSector();
            System.out.print(district + "\t");
            System.out.println("x: " + s.getX() + " y: " + s.getY() + " w: " + s.getWidth() + " h: " + s.getHeight());
        }
    }
}
