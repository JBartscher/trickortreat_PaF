package main.java.pathfinding;

import main.java.map.Map;
import main.java.map.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AStar {

    private Node[][] map;
    private Point startPosition;
    private Point targetPosition;
    private Heap<Node> openNodes;
    private Set<Node> closedNodes;
    private int mapDimension;

    public AStar(Map gameMap){
        mapDimension = gameMap.getSize();
        map = new Node[mapDimension][mapDimension];
        startPosition = new Point(0, 0);
        targetPosition = new Point(0, 0);
        openNodes = new Heap<Node>();
        closedNodes = new HashSet<Node>();
        fillMap(gameMap.getMap(), false);
    }

    public Node getMapCell(Point coord){
        //System.out.println(coord);
        return map[(int)coord.getY()][(int)coord.getX()];
    }

    public void setMapCell(Point coord, Node n){
        map[(int)coord.getY()][(int)coord.getX()] = n;
    }

    public Point getStartPosition(){
        return startPosition;
    }

    public Point getTargetPosition(){
        return targetPosition;
    }

    public void setStartPosition(Point coord){
        startPosition.setLocation(coord);
    }

    public void setTargetPosition(Point coord){
        targetPosition.setLocation(coord);
    }

    public int getDimension(){
        return map.length;
    }

    public void addToOpenNodes(Node n){
        n.setOpen();
        openNodes.add(n);
    }

    public Node popBestOpenNode(){
        return openNodes.remove();
    }

    public void addToClosedNodes(Node n){
        n.setClosed();
        closedNodes.add(n);
    }

    public boolean isInsideMap(Point p){
        return ( (p.getX() >= 0) && (p.getX() < getDimension())  && (p.getY() >= 0) && (p.getY() < getDimension()) );
    }

    public Set<Node> getNeighbours(Node n){


        /*
        Set<Node> neighbours = new HashSet<Node>();
        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if( !(i==0 && j==0) )
                    if(isInsideMap(new Point(n.getX() + i,n.getY() + j))){
                        Node temp = getMapCell(new Point(n.getX() + i,n.getY() +  j));
                        if(!temp.isObstacle())
                            neighbours.add(temp);
                    }
            }
        }
        return neighbours;
        */


        Set<Node> neighbours = new HashSet<>();
        for(int i=-1; i<=1; i++){
            if(i != 0)
                if(isInsideMap(new Point(n.getX() + i,n.getY()))){
                    Node temp = getMapCell(new Point(n.getX() + i,n.getY()));
                    if(!temp.isObstacle())
                        neighbours.add(temp);
            }
        }

        for(int i=-1; i<=1; i++){
            if(i != 0)
                if(isInsideMap(new Point(n.getX(),n.getY() + i))){
                    Node temp = getMapCell(new Point(n.getX() ,n.getY() + i));
                    if(!temp.isObstacle())
                        neighbours.add(temp);
                }
        }

        return neighbours;



    }

    static double calculateDistance(Point from, Point to){
        return Math.pow(Math.pow(from.getX()-to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2) , 0.5);
    }

    public ArrayList<Node> reconstructPath(Node target){
        ArrayList<Node> path = new ArrayList<Node>();
        Node current = target;
        //path.add(current);

        while(current.getParent() != null){
            path.add(current.getParent());
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public void printPath(ArrayList<Node> path){
        for(int i=0; i<path.size(); i++){
            Node node = path.get(i);
            System.out.println("node : (" + node.getX() + "," + node.getY() + ")");
        }
    }

    public ArrayList<Node> executeAStar(){
        Node start = getMapCell(getStartPosition());
        Node target = getMapCell(getTargetPosition());
        addToOpenNodes(start);

        start.setCostFromStart(0);
        start.setTotalCost( start.getCostFromStart() + calculateDistance(start.getPosition(), target.getPosition()) );
        while(!openNodes.isEmpty()){
            Node current = popBestOpenNode();
            if(current.equals(target)){
                return reconstructPath(target);
            }

            addToClosedNodes(current);
            Set<Node> neighbours = getNeighbours(current);
            for(Node neighbour : neighbours){
                if(!neighbour.isClosed()){
                    double tentativeCost = current.getCostFromStart() + calculateDistance(current.getPosition(), neighbour.getPosition());

                    if( (!neighbour.isOpen()) || (tentativeCost < neighbour.getCostFromStart()) ){
                        neighbour.setParent(current);
                        neighbour.setCostFromStart(tentativeCost);
                        neighbour.setTotalCost(neighbour.getCostFromStart() + calculateDistance(neighbour.getPosition(), start.getPosition()));
                        if(!neighbour.isOpen())
                            addToOpenNodes(neighbour);
                    }
                }
            }
        }

        return null;
    }

    /**
     * create a node map to find a path between two doors
     *
     * @param tileMap
     * @param ignoreObstacles
     */
    public void fillMapForStreetNetwork(Tile[][] tileMap, boolean ignoreObstacles) {

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                Node.Type type;

                if (tileMap[y][x].objectLayer.getImageNr() > 25 || tileMap[y][x].coverLayer.getImageNr() > 25 || (tileMap[y][x].objectLayer.getImageNr() < 0 && !ignoreObstacles) || tileMap[y][x].objectLayer.getImageNr() < -20) {
                    type = Node.Type.OBSTACLE;
                } else {
                    type = Node.Type.NORMAL;
                }

                map[y][x] = new Node(x, y, type);
            }
        }
    }

    /**
     * create a node Map to determine a path for the npc to hunt player objects
     *
     * @param tileMap
     * @param ignoreObstacles
     */
    public void fillMap(Tile[][] tileMap, boolean ignoreObstacles) {

        //System.out.println("Fill NodeMap");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Node.Type type;

                if (tileMap[y][x].objectLayer.getImageNr() > 26 || tileMap[y][x].coverLayer.getImageNr() > 26 || tileMap[y][x].objectLayer.getImageNr() < 0 || (tileMap[y][x].objectLayer.getImageNr() < 0 && !ignoreObstacles)) {
                    type = Node.Type.OBSTACLE;
                    //System.out.print("X ");
                } else {
                    type = Node.Type.NORMAL;
                    //System.out.print("O ");
                }

                map[y][x] = new Node(x, y, type);
            }
            //System.out.println("");
        }
    }

    public Node[][] getMap() {
        return map;
    }

    public void setMap(Node[][] map) {
        this.map = map;
    }
}