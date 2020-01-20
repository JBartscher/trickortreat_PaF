package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.Network.Event;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.Mansion;
import main.java.gameobjects.mapobjects.TownHall;
import main.java.map.Map;
import main.java.map.MapObject;
import main.java.map.Placeable;
import main.java.map.Tile;
import main.java.pathfinding.AStar;
import main.java.pathfinding.Node;
import main.java.pathfinding.PathWorker;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.round;

public class MovementManager implements EventHandler<InputEvent> {

    Player player1;
    Player player2;

    Player inputAWSD;
    Player inputARROW;
    Player inputMOUSE;

    Player witchTarget;

    private Map map;
    private Game game;
    private AStar aStar;


    // There are three different kinds of movement types
    public enum MovementType implements Serializable {
        KEYBOARD_AWSD, KEYBOARD_ARROW, MOUSE;
    }

    // represents the move directions to animate the entities
    public enum MoveDirection implements Serializable {
        DOWN, LEFT, RIGHT, UP
    }

    // Lokaler Multiplayer
    public MovementManager(Game game, Player player1, Player player2) {
        this.game = game;
        this.map = game.getMap();
        this.player1 = player1;
        this.player2 = player2;

        // register the player to movement strategies
        registerPlayerInputs(player1);
        registerPlayerInputs(player2);

        // init Pathfinding
        this.aStar = new AStar(game.getMap());

    }


    public void registerPlayerInputs(Player player) {
        if (player.movementType == MovementType.KEYBOARD_AWSD) {
            inputAWSD = player;

        } else if (player.movementType == MovementType.KEYBOARD_ARROW) {
            inputARROW = player;

        } else if (player.movementType == MovementType.MOUSE) {
            inputMOUSE = player;
        }
    }

    @Override
    public void handle(InputEvent event) {

        if (event.getClass() == MouseEvent.class) {
            handleMouse((MouseEvent) event);
        } else if (event.getClass() == KeyEvent.class) {
            handleKeyboard((KeyEvent) event);
        }
    }

    public void handleMouse(MouseEvent event) {
        if (inputMOUSE == null) return;
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (inputMOUSE != null) {

                double currentRenderX = inputMOUSE.xPos - inputMOUSE.getxOffSet() + Tile.TILE_SIZE / 2;
                double currentRenderY = inputMOUSE.yPos - inputMOUSE.getyOffSet() + Window.HEIGHT * 0.1 + Tile.TILE_SIZE / 2;
                if (inputMOUSE == game.getOtherPlayer()) currentRenderX += Game.WIDTH + 2 * Tile.TILE_SIZE;

                inputMOUSE.setTarget(inputMOUSE.getxPos() + (event.getSceneX() - currentRenderX), inputMOUSE.getyPos() + (event.getSceneY() - currentRenderY));
            }
        }
    }

    public void handleKeyboard(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getCode() == KeyCode.M) {
                Sound.muteSound();
            }

            if (event.getCode() == KeyCode.P) {
                if (game.gameMode == Game.GameMode.REMOTE) {
                    if (game.getGameController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                        ((NetworkController) game.getGameController()).changeGameStateObject("PAUSED", Event.EventType.PAUSED);
                        game.paused = true;
                    }
                } else if (game.getGameMode() == Game.GameMode.LOCAL) {
                    game.paused = true;
                }

            }

            if (event.getCode() == KeyCode.A) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.target.x - inputAWSD.speed * 100, inputAWSD.target.y);
                }
            }

            if (event.getCode() == KeyCode.W) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.target.x, inputAWSD.target.y - inputAWSD.speed * 100);
                }
            }

            if (event.getCode() == KeyCode.S) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.target.x, inputAWSD.target.y + inputAWSD.speed * 100);
                }
            }

            if (event.getCode() == KeyCode.D) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.target.x + inputAWSD.speed * 100, inputAWSD.target.y);
                }
            }

        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {

            if (event.getCode() == KeyCode.R) {
                if (game.gameMode == Game.GameMode.REMOTE) {
                    if (game.getGameController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                        ((NetworkController) game.getGameController()).changeGameStateObject("UNPAUSED", Event.EventType.UNPAUSED);
                        game.paused = false;
                    }
                } else if (game.getGameMode() == Game.GameMode.LOCAL) {
                    game.paused = false;
                }
            }

            if (event.getCode() == KeyCode.A) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.xPos, inputAWSD.target.y);
                }
            }

            if (event.getCode() == KeyCode.W) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.xPos, inputAWSD.yPos);
                }
            }

            if (event.getCode() == KeyCode.S) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.xPos, inputAWSD.yPos);
                }
            }

            if (event.getCode() == KeyCode.D) {
                if (inputAWSD != null) {
                    inputAWSD.setTarget(inputAWSD.xPos, inputAWSD.target.y);
                }
            }
        }


        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getCode() == KeyCode.LEFT) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.target.x - inputARROW.speed * 100, inputARROW.target.y);
                }
            }

            if (event.getCode() == KeyCode.UP) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.target.x, inputARROW.target.y - inputARROW.speed * 100);
                }
            }

            if (event.getCode() == KeyCode.DOWN) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.target.x, inputARROW.target.y + inputARROW.speed * 100);
                }
            }

            if (event.getCode() == KeyCode.RIGHT) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.target.x + inputARROW.speed * 100, inputARROW.target.y);
                }
            }


        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if (event.getCode() == KeyCode.LEFT) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.xPos, inputARROW.target.y);
                }
            }

            if (event.getCode() == KeyCode.UP) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.xPos, inputARROW.yPos);
                }
            }

            if (event.getCode() == KeyCode.DOWN) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.xPos, inputARROW.yPos);
                }
            }

            if (event.getCode() == KeyCode.RIGHT) {
                if (inputARROW != null) {
                    inputARROW.setTarget(inputARROW.xPos, inputARROW.target.y);
                }
            }
        }
    }

    /**
     * find the path for the nearest player for npc
     */
    public void findPath(Entity entity, Point start, Point target) {

        /**
         * set start and target position and find path
         */
        aStar.setStartPosition(start);
        aStar.setTargetPosition(target);
        aStar.fillMap(game.getMap().getMap(), false);

        CopyOnWriteArrayList<Point> targets = entity.getTargets();
        targets.clear();
        ArrayList<Node> nodes = aStar.executeAStar();

        if (nodes == null) {
            System.out.println("KEIN PFAD GEFUNDEN");
            return;
        }


        /**
         * add all nodes to the list of targets
         */
        for (Node node : nodes) {
            targets.add(node.getPosition());
        }

        /**
         * result of Astar algorithm does not contain the current position of the player
         * have to add manually to list of targets
         */
        if(witchTarget != null) {
            targets.add(witchTarget.getEntityPos());
        }



        // Terminieren, wenn kein Ziel existiert
        if (targets.size() < 1) return;

        /**
         * transform X/Y to pixel based cooridnates and set first and last target of npc
         */
        int transformedX = targets.get(0).x * Tile.TILE_SIZE;
        int transformedY = targets.get(0).y * Tile.TILE_SIZE;
        entity.setTarget(new Point(transformedX, transformedY));

        int transformedLastX = targets.get(targets.size() - 1).x * Tile.TILE_SIZE;
        int transformedLastY = targets.get(targets.size() - 1).y * Tile.TILE_SIZE;
        if (entity instanceof Witch) {
            Witch witch = (Witch) entity;
            witch.setFinalTargetPos(new Point(transformedLastX, transformedLastY));
        }
    }

    /**
     * find a target for npc
     */
    public Point chooseTarget(Witch witch, Player player, Player otherPlayer) {

        if (player.getChildrenCount() <= 0 && otherPlayer.getChildrenCount() <= 0) {
            witchTarget = null;
            return new Point(0, 0);

        }

        /**
         * if one player is inside a building or have no children then select the other player as a target
         */
        if (player.isInside() || player.getChildrenCount() <= 0) {

            if (otherPlayer.getChildrenCount() > 0) {
                witchTarget = otherPlayer;
                return otherPlayer.getEntityPos();
            } else {
                witchTarget = null;
                return new Point(0, 0); }

        } else if (otherPlayer.isInside() || otherPlayer.getChildrenCount() <= 0) {
            if (player.getChildrenCount() > 0) {
                witchTarget = player;
                return player.getEntityPos();
            } else {
                witchTarget = null;
                return new Point(0, 0);
            }
        }

        /**
         * find the nearest player and set entity position as target
         */
        double distancePlayer = Math.sqrt((player.getxPos() - witch.getxPos()) * (player.getxPos() - witch.getxPos()) + (player.getyPos() - witch.getyPos()) * (player.getyPos() - witch.getyPos()));
        double distanceOtherPlayer = Math.sqrt((otherPlayer.getxPos() - witch.getxPos()) * (otherPlayer.getxPos() - witch.getxPos()) + (otherPlayer.getyPos() - witch.getyPos()) * (otherPlayer.getyPos() - witch.getyPos()));
        if (distancePlayer < distanceOtherPlayer) {
            witchTarget = player;
            return player.getEntityPos();
        } else {
            witchTarget = otherPlayer;
            return otherPlayer.getEntityPos();
        }
    }


    /**
     * checks if witch reached the interim goal and : if so, then set next interim goal
     * @param entity
     * @param movementSize
     */
    public void checkTarget(Entity entity, double movementSize) {

        if (entity instanceof Witch) {
            Witch witch = (Witch) entity;
            if (Math.abs(witch.getxPos() - witch.getHomeX()) <= Tile.TILE_SIZE && Math.abs(witch.getyPos() - witch.getHomeY()) <= Tile.TILE_SIZE) {
                witch.setOnReturn(false);
            }
        }

        CopyOnWriteArrayList<Point> targets = entity.getTargets();
        if (targets.size() > 0) {
            Point transformedPoint = new Point((int) round(entity.getTarget().x / Tile.TILE_SIZE), (int) round(entity.getTarget().y / Tile.TILE_SIZE));
            //if( Math.abs(entity.getxPos() - entity.getTarget().x) < movementSize * 0.1 && Math.abs(entity.getyPos() - entity.getTarget().y) < movementSize * 0.1 ) {
            if (entity.getEntityPos().x == transformedPoint.x && entity.getEntityPos().y == transformedPoint.y) {

                /** remove interim goal from target list and set new interim goal
                 *
                 */
                if (targets.size() > 1) {
                    targets.remove(0);
                    if (targets.size() > 0) {
                        int transformedX = targets.get(0).x * Tile.TILE_SIZE;
                        int transformedY = targets.get(0).y * Tile.TILE_SIZE;
                        entity.setTarget(new Point(transformedX, transformedY));
                    }
                }
            }
        }
    }

    /**
     * move an entity depending on move direction, entity speed and their current target(s)
     * @param entity
     */
    public void moveObject(Entity entity) {
        double movementSize = entity.getSpeed() / Game.FRAMES;

        /**
         * if entity is the npc -> check for targets
         */
        if (entity == game.getWitch()) checkTarget(entity, movementSize);


        double moveX = 0.0;
        double moveY = 0.0;

        /**
         * move into X-direction
         */
        if (entity.target.x > entity.getxPos()) {
            if (entity.target.x - entity.getxPos() < movementSize) {
                moveX = entity.target.x - entity.getxPos();
            } else {
                moveX = movementSize;
            }
            entity.setMoveDirection(MoveDirection.RIGHT);

        } else if (entity.target.x < entity.getxPos()) {
            if (Math.abs(entity.target.x - entity.getxPos()) < movementSize) {
                moveX = -Math.abs(entity.target.x - entity.getxPos());
            } else {
                moveX = -movementSize;
                entity.setMoveDirection(MoveDirection.LEFT);
            }

        }

        /**
         * move into Y-direction
         */
        if (entity.target.y > entity.getyPos()) {
            if (entity.target.y - entity.getyPos() < movementSize) {
                moveY = entity.target.y - entity.getyPos();
            } else {
                moveY = movementSize;
            }
            entity.setMoveDirection(MoveDirection.DOWN);

        } else if (entity.target.y < entity.getyPos()) {
            if (Math.abs(entity.target.y - entity.getyPos()) < movementSize) {
                moveY = -Math.abs(entity.target.y - entity.getyPos());
            } else {
                moveY = -movementSize;
                entity.setMoveDirection(MoveDirection.UP);
            }
        }


        /**
         * check for out of bounds or collisions
         */
        entity.setxPos(entity.getxPos() + moveX);
        if (outOfBounds(entity)) {
            entity.setxPos(entity.getxPos() - moveX);
        }

        entity.setyPos(entity.getyPos() + moveY);
        if (outOfBounds(entity)) {
            entity.setyPos(entity.getyPos() - moveY);
        }

        // checks for collisions and events
        moveHorizontal(moveX, entity);
        moveVertical(moveY, entity);

        /**
         * update the entity animation image
         */
        entity.setEntityImage(false);
    }

    /**
     * moves all entities and calculate costs for pathfinding
     * @param gameController
     * @param listOPlayers
     * @param witch
     */
    public void moveAllEntites(GameController gameController, CopyOnWriteArrayList<Player> listOPlayers, Witch witch) {
        for (Player player : listOPlayers) {
            if (player.getChildrenCount() > 0)
                moveObject(player);
        }

        moveWitch(gameController, witch);

    }

    /**
     * move the npc
     * @param gameController
     * @param witch
     */
    private void moveWitch(GameController gameController, Witch witch) {

        //move NPC
        if ((game.getGameMode() == Game.GameMode.LOCAL || gameController.getNetworkRole() == NetworkController.NetworkRole.SERVER) && game.DRAMATIC) {
            if (game.ticks % 10 == 0 /*|| game.ticks == 1 */) {

                Point target = chooseTarget(game.getWitch(), game.getPlayer(), game.getOtherPlayer());
                Point start = witch.getEntityPos();

                boolean doPathfinding = true;
                double deltaTargetX = Math.abs(target.x - witch.getFinalTargetPos().x);
                double deltaTargetY = Math.abs(target.y - witch.getFinalTargetPos().y);

                double deltaPosTargetX = Math.abs(witch.getxPos() - witch.getFinalTargetPos().x);
                double deltaPosTargetY = Math.abs(witch.getxPos() - witch.getFinalTargetPos().y);

                /*
                if(game.ticks == 2) {
                    doPathfinding = true;
                }
                else if (deltaTargetX <= 0.5 * Tile.TILE_SIZE && deltaTargetY <= 0.5 * Tile.TILE_SIZE) {
                    doPathfinding = false;
                } else if(  (deltaPosTargetX > game.getMap().getSize() * 0.2 && game.ticks % 20 != 0) || (deltaPosTargetY > game.getMap().getSize() * 0.2 && game.ticks % 20 != 0)   ) {
                    doPathfinding = false;
                }

                 */

                moveObject(witch);


                if (doPathfinding) {

                    if (!witch.isOnReturn()) {
                        if (witch.getFinalTargetPos() != target) {
                            new PathWorker(witch, start, target, this).start();
                        }
                    } else {
                        if (witch.getFinalTargetPos() != witch.getHomePos()) {
                            new PathWorker(witch, start, witch.getHomePos(), this).start();
                        }
                    }
                }
            }
        }
    }


    /**
     * move entity objects in vertical direction
     * checks for collisions with houses and doors
     */
    public void moveVertical(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);

        if ((map.getMapSector().intersectsWithContainingItems(p) ||
                game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].getTileNr() < 0 ||
                collideWithKey(entity)
        )) {

            if (collideWithKey(entity)) collectKey(entity);

            int yOffset = 0;
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (player.isInside()) {
                    yOffset = -1;
                }
            }

            checkCollisionWithDoor(p, entity);

            // revert movement when entity is not a player and has a collision detection
            if (entity instanceof Player && ((Player) entity).isNoCollision() && !collideWithKey(entity)) {

            } else {
                //System.out.println("COLLIDE!");
                entity.setyPos(entity.getyPos() - size);
            }

        } else {
            checkCollisionsBetweenEntities(entity, size, false);
        }
    }

    public boolean collideWithKey(Entity entity) {
        return (entity.getEntityPos().x == 31 && entity.getEntityPos().y == 29);
    }

    public void collectKey(Entity entity) {

        for (MapObject o : game.getMap().getMapSector().getAllContainingMapObjects()) {
            if (o instanceof TownHall) {
                TownHall t = (TownHall) o;
                t.takeKey((Player) entity);
                game.getMap().getMap()[29][31][1].setTileNr(133);
                break;
            }
        }
    }

    /**
     * move entity objects in horizontal direction
     * checks for collisions with houses and doors
     */
    public void moveHorizontal(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);
        if ((map.getMapSector().intersectsWithContainingItems(p) && !entity.isNoCollision() ||
                game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].getTileNr() < 0)
                || collideWithKey(entity)
        ) {

            // revert movement when entity is not a player and has a collision detection
            if (entity instanceof Player && ((Player) entity).isNoCollision() && !collideWithKey(entity)) {

            } else {
                //System.out.println("COLLIDE!");
                entity.setxPos(entity.getxPos() - size);
            }


            // TODO: LÖST die Kollision zwischen zwei Spielern
        } else {

            // überprüft die Kollision zwischen Entitäten
            checkCollisionsBetweenEntities(entity, size, true);
        }
    }

    public void checkCollisionWithDoor(Placeable p, Entity entity) {


        // collision with door
        if (map.getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].isDoorTile()) {
            for (MapObject obj : map.getMapSector().getAllContainingMapObjects()) {
                try {
                    House h = (House) obj;
                    if (h.intersects(p)) {
                        if ((entity instanceof Player && h.isUnvisited() || (entity instanceof Player && obj instanceof Mansion && entity == ((Mansion) h).insidePlayer) || (entity instanceof Player && obj instanceof TownHall) || entity instanceof Player && obj instanceof GingerbreadHouse)) {
                            h.visit((Player) entity);
                        }
                    }
                } catch (ClassCastException ex) {
                    // the Object is not a House
                    continue;
                }
            }
        }


    }

    public void checkCollisionsBetweenEntities(Entity entity, double size, boolean directionX) {
        for (Entity e : game.getListOfAllEntities()) {
            if (e == entity) continue;

            double offset = 0.5;
            if (e instanceof Witch) {
                offset = 1.2;
            } else {
                offset = 0.5;
            }

            if (Math.abs(entity.getxPos() - e.getxPos()) < offset * Tile.TILE_SIZE && Math.abs(entity.getyPos() - e.getyPos()) < Tile.TILE_SIZE * offset) {
                if (e instanceof AliceCooper && entity instanceof Player) {
                    ((AliceCooper) e).playSong((Player) entity);
                } else if (e instanceof Witch && entity instanceof Player) {
                    Witch witch = (Witch) e;
                    Player player = (Player) entity;
                    if (player.getChildrenCount() <= 0) return;

                    if (player.getProtectedTicks() > 0) {
                        System.out.println("NO COLLISION WEGEN PROTECTION!!");
                        return;
                    }

                    Sound.playChild();

                    witch.setOnReturn(true);
                    player.setChildrenCount(player.getChildrenCount() - 1);
                    player.setProtectedTicks(100);

                    if (game.getGameMode() == Game.GameMode.REMOTE) {
                        ((NetworkController) game.getGameController()).changeGameStateObject(witch, Event.EventType.COLLISION);
                    }
                }

                if (directionX) {
                    entity.setxPos(entity.getxPos() - size);
                } else {
                    entity.setyPos(entity.getyPos() - size);
                }

            }
        }
    }


    /**
     * checks if the new player position would be out of bounds
     *
     * @return true if out of bounds failing which false
     */
    private boolean outOfBounds(Entity entity) {

        if (entity instanceof Player && ((Player) entity).isInside()) {
            Player player = (Player) entity;
            return outOfBoundsInside((Player) entity, player.getInsideObject());

        } else {

            Placeable p1 = new Placeable(entity.getEntityPosWithCurrency(-0.1).y, entity.getEntityPosWithCurrency(-0.1).x, 1, 1, 0);
            Placeable p2 = new Placeable(entity.getEntityPosWithCurrency(+0.33).y, entity.getEntityPosWithCurrency(+0.33).x, 1, 1, 0);

            // mapSector does not contain player anymore
            return !map.getMapSector().intersects(p1) || !map.getMapSector().intersects(p2);
        }
    }

    private boolean outOfBoundsInside(Player player, MapObject o) {

        int e_x = player.getEntityPos().x;
        int e_y = player.getEntityPos().y;
        int o_x = o.getY();
        int o_y = o.getX();
        int o_width = o.getHeight();
        int o_height = o.getWidth();

        return (
                (e_x >= o_x && e_x <= o_x + o_width && e_y == o_y) ||
                        (e_x >= o_x && e_x <= o_x + o_width && player.getEntityPosWithCurrency(+0.5).y == o_y + o_height) ||

                        (player.getEntityPosWithCurrency( -.1).x == o_x - 1 && e_y >= o_y && e_y <= o_y + o_height) ||
                        (player.getEntityPosWithCurrency(+.33).x == o_x + o_width && e_y >= o_y && e_y <= o_y + o_height)

        );
    }



}

