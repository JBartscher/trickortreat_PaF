package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Entity;
import main.java.gameobjects.Player;

/**
 * Interface for Decorator Pattern implementation
 */
public interface Visitable {

    void visit(Player player);

    void repaintAfterVisit();
}
