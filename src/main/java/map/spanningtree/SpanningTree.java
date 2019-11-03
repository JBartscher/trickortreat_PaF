package map.spanningtree;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.BoruvkaMinimumSpanningTree;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpanningTree {

    /**
     * creates a fully connected graph, eg. every vertex is connected to every other
     * vertex. The weight is represented through the distance between two points/vertexes
     *
     * @param points
     * @return
     */
    public Graph<Point, WeightedEdge> createFullyConnectedGraph(ArrayList<Point> points) {

        //create a default graph with points as vertexes

        Graph<Point, WeightedEdge> myGraph = new DirectedWeightedPseudograph<>(WeightedEdge.class);

        //add all points as vertexes
        points.forEach(point -> myGraph.addVertex(point));
        //connect each and every vertex with all other vertexes
        for (Point p_s : points) {
            for (Point p_e : points) {
                double distance = (double) p_s.distance(p_e);
                WeightedEdge wE = new WeightedEdge(distance / 100);
                myGraph.addEdge(p_s, p_e, wE);
                myGraph.setEdgeWeight(wE, distance / 100);
            }

        }
        return myGraph;
    }

    public SpanningTreeAlgorithm.SpanningTree KruskalSpanningTree(Graph g) {
        KruskalMinimumSpanningTree spanningTreeAlg = new KruskalMinimumSpanningTree(g);
        SpanningTreeAlgorithm.SpanningTree spanningTree = spanningTreeAlg.getSpanningTree();
        return spanningTree;
    }

    public SpanningTreeAlgorithm.SpanningTree BoruvkaSpanningTree(Graph g) {
        BoruvkaMinimumSpanningTree spanningTreeAlg = new BoruvkaMinimumSpanningTree(g);
        SpanningTreeAlgorithm.SpanningTree spanningTree = spanningTreeAlg.getSpanningTree();
        return spanningTree;
    }

    public SpanningTreeAlgorithm.SpanningTree PrimSpanningTree(Graph g) {
        PrimMinimumSpanningTree spanningTreeAlg = new PrimMinimumSpanningTree(g);
        SpanningTreeAlgorithm.SpanningTree spanningTree = spanningTreeAlg.getSpanningTree();
        return spanningTree;
    }

    /**
     * takes a List of Edges and constructs a new Graph from them.
     *
     * @param edgeList
     * @return Graph
     */
    public Graph<Point, WeightedEdge> constructGraphFromEdgeList(List<WeightedEdge> edgeList) {
        Graph<Point, WeightedEdge> graph = new DirectedWeightedPseudograph<Point, WeightedEdge>(WeightedEdge.class);
        edgeList.forEach(edge -> graph.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge)));
        return graph;
    }
}
