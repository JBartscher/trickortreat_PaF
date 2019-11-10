package map;

public class TestMapPrinter {

    static void printMap(Map map){
        for(int x = 0; x < map.map[0].length; x++){
            for(int y = 0; y < map.map.length; y++){
                System.out.print(map.map[x][y].tileNr);
            }
            System.out.print("\n");
        }
    }
}
