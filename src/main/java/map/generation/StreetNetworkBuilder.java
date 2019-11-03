package map.generation;

import map.spanningtree.SpanningTree;
import map.spanningtree.WeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * To create a Streetmap we need to:
 * 1. create the four Entrypoints to the map (to, left,right.bottom) and add them to our point list to ensure
 * that the cell is reachable from every entrypoint.
 * 2. add a amount n of points, best a little normalized so there are no "bulks" of points.
 * 3. create a graph from those and the entry points
 * 4. find the spinning tree (Kruskal´s Algorithm see https://de.wikipedia.org/wiki/Algorithmus_von_Kruskal) for our points and save those as point pair.
 * 5. create a Delauny Graph and add around ~8-15% of its edges as point-paris to our point pair list
 * 6. connect the point pairs via an 'L' Shape and mark all cells in the map under the 'L' as Street.
 * 7. determine the connections of a street and give it the right street image (some transformation logic might be necessary)
 *
 * @author bartscher
 */
public class StreetNetworkBuilder {

    private final Point ENTRY_TOP;
    private final Point ENTRY_BOTTOM;
    private final Point ENTRY_LEFT;
    private final Point ENTRY_RIGHT;
    private ArrayList<Point> points = new ArrayList<Point>();

    public StreetNetworkBuilder(int cellSize) {

        //must be uneven so that the entrances are centered
        if (cellSize % 2 == 0) {
            throw new IllegalArgumentException("Size of Cell must be uneaven!");
        }

        final int middle = (cellSize / 2) + 1;

        ENTRY_TOP = new Point(middle, 0);
        ENTRY_BOTTOM = new Point(middle, cellSize);
        ENTRY_LEFT = new Point(0, middle);
        ENTRY_RIGHT = new Point(cellSize, middle);

        //add initial start points
        points.add(ENTRY_TOP);
        points.add(ENTRY_BOTTOM);
        points.add(ENTRY_LEFT);
        points.add(ENTRY_RIGHT);
        //add a number of random Points to the point list
        points.addAll(randPoints(cellSize / 4));
    }

    public SpanningTreeAlgorithm.SpanningTree getSpanningTree() {
        SpanningTree sP = new SpanningTree();
        Graph<Point, map.spanningtree.WeightedEdge> g = sP.createFullyConnectedGraph(points);
        SpanningTreeAlgorithm.SpanningTree spanningTree = sP.PrimSpanningTree(g);
        //return g.edgeSet();
        return spanningTree;
    }

    public void randomlyConnectSomeEdges(SpanningTreeAlgorithm.SpanningTree sT, double faktor) {
        Random r = new Random();
        int numberOfEdges = sT.getEdges().size();
        // number of edges that will be randomly added
        int newEdgeCount = (int) (numberOfEdges / faktor);

        SpanningTree sP = new SpanningTree();

        //create a graph from the spanning tree
        DirectedWeightedPseudograph baseGraph = (DirectedWeightedPseudograph) sP.constructGraphFromEdgeList((java.util.List<WeightedEdge>) sT.getEdges());

        List vertexes = (List) baseGraph.vertexSet();
        for (int i = 0; i < numberOfEdges; i++) {
            // Point p1 = vertexes.getItem(r.nextInt());
            // WeightedEdge newEdge = new WeightedEdge(0);
            // baseGraph.addEdge()
        }
    }

    public void setLShapedStreetsToMap() {

    }

    /**
     * creates a random points in the cell
     *
     * @param n
     * @return
     */
    private ArrayList<Point> randPoints(int n) {
        ArrayList<Point> randomPoints = new ArrayList<>();
        Random r = new Random();
        int x, y;

        for (int i = 0; i < n; i++) {
            x = r.nextInt(27);
            y = r.nextInt(27);

            randomPoints.add(new Point(x, y));
        }
        return randomPoints;
    }
}
