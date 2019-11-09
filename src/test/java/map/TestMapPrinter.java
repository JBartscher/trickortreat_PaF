package map;

public class TestMapPrinter {

    static void printMap(Map map){
        for(int i = 0; i < map.map[0].length; i++){
            for(int j = 0; j < map.map.length; j++){
                System.out.print(map.map[i][j]);
            }
            System.out.print("\n");
        }
    }
}
