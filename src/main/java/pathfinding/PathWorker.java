package main.java.pathfinding;

import main.java.Game;
import main.java.MovementManager;
import main.java.Singleton;
import main.java.gameobjects.Entity;
import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.GingerbreadHouse;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class uses a thread pool to calculate a path to the chosen target
 */
public class PathWorker {

    private MovementManager movementManager;
    ExecutorService executor = Executors.newCachedThreadPool();



    public PathWorker(Game game, MovementManager movementManager)
    {
        this.movementManager = movementManager;
    }

    /**
     * set path data and execute task with thread from pool
     * @param entity
     * @param start
     * @param end
     */
    public void execute(Entity entity, Point start, Point end) {

        Runnable r = () -> {
            System.out.println(Thread.currentThread());
            movementManager.findPath(entity, start, end);
        };

        executor.execute(r);
    }
}
