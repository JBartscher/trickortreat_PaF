package main.java.pathfinding;

import main.java.MovementManager;
import main.java.gameobjects.Entity;

import java.awt.*;

public class PathWorker extends Thread {

    private Entity entity;
    private Point start;
    private Point end;
    private MovementManager movementManager;


    public PathWorker(Entity entity, Point start, Point end, MovementManager movementManager)
    {
        this.entity = entity;
        this.start = start;
        this.end = end;
        this.movementManager = movementManager;

    }


    public void run () {
        movementManager.findPath(entity, start, end);
    }




}
