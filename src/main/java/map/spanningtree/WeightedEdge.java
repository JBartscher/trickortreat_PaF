package map.spanningtree;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdge extends DefaultWeightedEdge {

    double weight;

    public WeightedEdge(double weight) {
        this.weight = weight;
    }

    public double getEdgeWeight() {
        return this.weight;
    }
}
