package map;

import main.java.map.Map;

public class TestMapPrinter {

    static void printMap(Map map) {
        for (int x = 0; x < map.getMap()[0].length; x++) {
            for (int y = 0; y < map.getMap().length; y++) {
                System.out.print(map.getMap()[x][y].getTileNr());
            }
            System.out.print("\n");
        }
    }
}
