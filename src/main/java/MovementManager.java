package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.Menu.GameMenu;
import main.java.Network.Event;
import main.java.Network.NetworkController;
import main.java.gameobjects.AliceCooper;
import main.java.gameobjects.Entity;
import main.java.gameobjects.Player;
import main.java.gameobjects.Witch;
import main.java.gameobjects.mapobjects.*;
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

    private PathWorker pathWorker;

    private final static Configuration<Object> config = new Configuration<Object>();

    /**
     * define the 3 types of movement in a enumeration
     */
    public enum MovementType implements Serializable {
        KEYBOARD_AWSD, KEYBOARD_ARROW, MOUSE
    }

    // represents the move directions to animate the entities
    public enum MoveDirection implements Serializable {
        DOWN, LEFT, RIGHT, UP
    }

    /**
     * constructor of MovementManager class
     * register the player for a selected movement type
     *
     * @param game
     * @param player1
     * @param player2
     */
    public MovementManager(Game game, Player player1, Player player2) {
        this.game = game;
        this.map = game.getMap();
        this.player1 = player1;
        this.player2 = player2;

        // register the player to movement type
        registerPlayerInputs(player1);
        registerPlayerInputs(player2);

        // init Pathfinding
        this.aStar = new AStar(game.getMap());
        this.pathWorker = new PathWorker(game, this);

    }

    /**
     * register the movement of a player object
     *
     * @param player
     */
    public void registerPlayerInputs(Player player) {
        if (player.movementType == MovementType.KEYBOARD_AWSD) {
            inputAWSD = player;

        } else if (player.movementType == MovementType.KEYBOARD_ARROW) {
            inputARROW = player;

        } else if (player.movementType == MovementType.MOUSE) {
            inputMOUSE = player;
        }
    }

    /**
     * check if the witch reached a interim goal. If that is the case then set next intermin goal as her main target
     *
     * @param entity
     * @param movementSize
     */
    public void checkTarget(Entity entity, double movementSize) {

        //if(game.getWitch().getTargets().size() < 5)
        //   System.out.println(game.getWitch().isOnReturn() + " Hexe-Ziel: " + game.getWitch().getFinalTargetPos() + " - Pos: " + game.getWitch().getEntityPos() + " -  Ziele: " + game.getWitch().getTargets());

        /**
         * check if the witch reached the door of her home - set return to false if that is the case
         */
        if (entity instanceof Witch) {
            Witch witch = (Witch) entity;
            if (Math.abs(witch.getxPos() - witch.getHomeX()) < 0.2 * Tile.TILE_SIZE && Math.abs(witch.getyPos() - witch.getHomeY()) < 0.2 * Tile.TILE_SIZE && witch.isOnReturn()) {
                //if ( witch.getFinalTargetPos().x == witch.getEntityPos().x && witch.getFinalTargetPos().y == witch.getEntityPos().y && witch.isOnReturn() ) {
                witch.setOnReturn(false);
                return;
            }
        }

        CopyOnWriteArrayList<Point> targets = entity.getTargets();
        if (targets.size() > 0) {
            Point transformedPoint = new Point(round(entity.getTarget().x / Tile.TILE_SIZE), round(entity.getTarget().y / Tile.TILE_SIZE));
            //if( Math.abs(entity.getxPos() - entity.getTarget().x) < movementSize * 0.1 && Math.abs(entity.getyPos() - entity.getTarget().y) < movementSize * 0.1 ) {
            if (entity.getEntityPos().x == transformedPoint.x && entity.getEntityPos().y == transformedPoint.y) {

                /** remove interim goal from target list and set new interim goal
                 *
                 */
                if (targets.size() > 1) {
                    targets.remove(0);
                    if (targets.size() > 0) {
                        int transformedX = (int) (targets.get(0).x * Tile.TILE_SIZE);
                        int transformedY = (int) (targets.get(0).y * Tile.TILE_SIZE);
                        entity.setTarget(new Point(transformedX, transformedY));
                    }
                }
            }
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

    private void checkButtonClicked(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED && event.getSceneX() < 70 && event.getSceneY() < 70) {
            String pausedEvent = "";
            boolean paused = game.paused;
            Event.EventType eventType;
            if (paused) {
                game.paused = false;
                paused = false;
                eventType = Event.EventType.UNPAUSED;
                pausedEvent = "UNPAUSED";
            } else {
                game.paused = true;
                paused = true;
                eventType = Event.EventType.PAUSED;
                pausedEvent = "PAUSED";
            }

            game.paused = paused;
            if (game.gameMode == Game.GameMode.REMOTE) {
                System.out.println("EVENT VERSCHICKEN!");
                ((NetworkController) game.getGameController()).changeGameStateObject(pausedEvent, eventType);
            }
            game.getLauncher().getMainMenu().showPausedMenu(game.getWindow().getScene());

            config.setParam("paused", game.paused);
            //if (!(Boolean)config.getParam("muted")) Sound.muteSound();
            GameMenu.setRightButtons();

            return;
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED && event.getSceneX() >= 70 && event.getSceneX() < 140 && event.getSceneY() < 70) {
            Sound.muteSound();
            GameMenu.setRightButtons();
            return;
        }
    }

    /**
     * process inputs via mouse and delegates the mouse event to the right object
     *
     * @param event
     */
    public void handleMouse(MouseEvent event) {

        checkButtonClicked(event);

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
                if (!(Boolean) config.getParam("muted")) Sound.muteSound();
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
                if ((Boolean) config.getParam("muted")) Sound.muteSound();
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

        ArrayList<Node> nodes = aStar.executeAStar();
        CopyOnWriteArrayList<Point> targets = entity.getTargets();
        targets.clear();

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

        Node[][] nodeMap = aStar.getMap();
        for (Point t : targets) {

            if (nodeMap[t.y][t.x].isObstacle()) {
                System.out.println("VERKEHRT!");
            }
        }

        /**
         * result of Astar algorithm does not contain the current position of the player
         * have to add manually to list of targets
         */
        if (witchTarget != null && !game.getWitch().isOnReturn()) {
            targets.add(witchTarget.getEntityPos());
        } else if (witchTarget != null && game.getWitch().isOnReturn()) {
            targets.add(new Point((int) (game.getWitch().getHomeX() / Tile.TILE_SIZE), (int) game.getWitch().getHomeY() / Tile.TILE_SIZE));
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

        if (witch.isOnReturn()) {
            return new Point((int) (witch.getHomeX() / Tile.TILE_SIZE), (int) (witch.getHomeY() / Tile.TILE_SIZE));
        }

        if (player.getChildrenCount() <= 0 && otherPlayer.getChildrenCount() <= 0) {
            witchTarget = null;
            return new Point((int) witch.getHomeX() / Tile.TILE_SIZE, (int) witch.getHomeY() / Tile.TILE_SIZE);
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
                return new Point((int) witch.getHomeX() / Tile.TILE_SIZE, (int) witch.getHomeY() / Tile.TILE_SIZE);
            }

        } else if (otherPlayer.isInside() || otherPlayer.getChildrenCount() <= 0) {
            if (player.getChildrenCount() > 0) {
                witchTarget = player;
                return player.getEntityPos();
            } else {
                witchTarget = null;
                return new Point((int) witch.getHomeX() / Tile.TILE_SIZE, (int) witch.getHomeY() / Tile.TILE_SIZE);
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
     * move the npc
     *
     * @param gameController
     * @param witch
     */
    private void moveWitch(GameController gameController, Witch witch) {

        Point start = witch.getEntityPos();

        //move NPC
        if ((game.getGameMode() == Game.GameMode.LOCAL || gameController.getNetworkRole() == NetworkController.NetworkRole.SERVER) && Game.DRAMATIC) {
            if (game.DRAMATIC) {
                moveObject(witch);
            }

            /**
             * call pathfinding algorithm for npc
             */
            if (game.ticks % 7 == 0 /*|| game.ticks == 1 */) {

                Point target = chooseTarget(game.getWitch(), game.getPlayer(), game.getOtherPlayer());

                boolean doPathfinding = true;
                double deltaTargetX = Math.abs(target.x - witch.getFinalTargetPos().x / Tile.TILE_SIZE);
                double deltaTargetY = Math.abs(target.y - witch.getFinalTargetPos().y / Tile.TILE_SIZE);

                double deltaPosTargetX = Math.abs(witch.getxPos() - witch.getFinalTargetPos().x);
                double deltaPosTargetY = Math.abs(witch.getxPos() - witch.getFinalTargetPos().y);

                if (witch.getTargets().size() > 0 && deltaTargetX < 2 && deltaTargetY < 2 &&
                        deltaPosTargetX > 200 && deltaPosTargetY > 200 &&
                        game.ticks % 20 != 0) {
                    doPathfinding = false;
                }


                if (doPathfinding) {

                    if (!witch.isOnReturn()) {
                        if (witch.getFinalTargetPos() != target) {
                            pathWorker.execute(witch, start, target);
                        }
                    } else {
                        if (witch.getFinalTargetPos() != witch.getHomePos()) {
                            pathWorker.execute(witch, start, witch.getHomePos());
                        }
                    }
                }
            }
        }

    }

    /**
     * move an entity depending on move direction, entity speed and their current target(s)
     *
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
     *
     * @param gameController
     * @param listOPlayers
     * @param witch
     */
    public void moveAllEntities(GameController gameController, CopyOnWriteArrayList<Player> listOPlayers, Witch witch) {
        for (Player player : listOPlayers) {
            if (player.getChildrenCount() > 0)
                moveObject(player);
        }
        moveWitch(gameController, witch);

    }

    /**
     * move entity objects in vertical direction
     * checks for collisions with houses and doors
     */
    public void moveVertical(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);

        if ((map.getMapSector().entityIntersectsWithContainingItems(entity) ||
                game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].getTileNr() < 0 ||
                collideWithKey(entity)
        )) {

            if (collideWithKey(entity)) collectKey(entity);

            checkCollisionWithDoor(p, entity);

            // revert movement when entity is not a player and has a collision detection
            if (entity instanceof Player && entity.isNoCollision() && !collideWithKey(entity) && !collideWithAliceCooper(entity)) {
                // pass
            } else {
                //System.out.println("COLLIDE!");
                entity.setyPos(entity.getyPos() - size);
                if(entity instanceof Witch) {
                    Point target = chooseTarget((Witch)entity, game.getPlayer(), game.getOtherPlayer());
                    findPath(entity, entity.getEntityPos(), target);
                }
            }

        } else {
            checkCollisionsBetweenEntities(entity, size, false);
        }
    }

    /**
     * check if the player collided with the sprite of Alice Cooper
     *
     * @param entity
     * @return
     */
    public boolean collideWithAliceCooper(Entity entity) {
        return (entity.getEntityPos().x == 5 && entity.getEntityPos().y == 5);
    }

    /**
     * move entity objects in horizontal direction
     * checks for collisions with houses and doors
     */
    public void moveHorizontal(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);

        if ((map.getMapSector().entityIntersectsWithContainingItems(entity) && !entity.isNoCollision() ||
                game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].getTileNr() < 0)
                || collideWithKey(entity)
        ) {
            // revert movement when entity is not a player and has a collision detection
            if (entity instanceof Player && entity.isNoCollision() && !collideWithKey(entity)) {

            } else {
                //System.out.println("COLLIDE!");
                entity.setxPos(entity.getxPos() - size);
                if(entity instanceof Witch) {
                    Point target = chooseTarget((Witch)entity, game.getPlayer(), game.getOtherPlayer());
                    findPath(entity, entity.getEntityPos(), target);
                }
            }

            // TODO: LÖST die Kollision zwischen zwei Spielern
        } else {

            // überprüft die Kollision zwischen Entitäten
            checkCollisionsBetweenEntities(entity, size, true);
        }
    }

    /**
     * check if the player collect the key within the town hall
     *
     * @param entity
     * @return
     */
    public boolean collideWithKey(Entity entity) {
        return (entity.getEntityPos().x == 31 && entity.getEntityPos().y == 29);
    }

    /**
     * collect the key within the town hall if key is available
     *
     * @param entity
     */
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
     * check if the player collided with a door tile
     */
    public void checkCollisionWithDoor(Placeable p, Entity entity) {
        if (map.getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].isDoorTile()) {
            for (MapObject obj : map.getMapSector().getAllContainingMapObjects()) {
                try {
                    House h = (House) obj;
                    SoundDecorator houseSoundDecorator = new SoundDecorator(h);
                    CandyDecorator houseCandyDecorator = new CandyDecorator(h);
                    /**
                     * call the visit method on the house object
                     */
                    if (h.intersects(p)) {
                        if ((entity instanceof Player && h.isUnvisited() || (entity instanceof Player && obj instanceof Mansion && entity == ((Mansion) h).insidePlayer) || (entity instanceof Player && obj instanceof TownHall) || entity instanceof Player && obj instanceof GingerbreadHouse)) {
                            /**
                             * the order of the call is not trivial and should be followed,
                             * because the soundDecorator does not care if a house has already been visited,
                             * the CandyDecorator does.
                             */
                            houseCandyDecorator.visit((Player) entity);
                            houseSoundDecorator.visit((Player) entity);
                        }
                    }
                    /**
                     * show a error message when the object is not a house
                     */
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        }
    }

    /**
     * check collisions between the players and between players and the npc
     *
     * @param entity
     * @param size
     * @param directionX
     */
    public void checkCollisionsBetweenEntities(Entity entity, double size, boolean directionX) {
        for (Entity e : game.getListOfAllEntities()) {
            if (e == entity) continue;

            /**
             * the collision with the witch occurred a little bit early
             */
            double offset = 0.5;
            if (e instanceof Witch) {
                offset = 0.7;
            } else {
                offset = 0.5;
            }

            /**
             * when the distance in x and y direction between entities is lower than the product of offset and Tile size
             * a collision occurred!
             */
            if (Math.abs(entity.getxPos() - e.getxPos()) < offset * Tile.TILE_SIZE && Math.abs(entity.getyPos() - e.getyPos()) < Tile.TILE_SIZE * offset) {
                if (e instanceof AliceCooper && entity instanceof Player) {
                    ((AliceCooper) e).playSong((Player) entity);

                    /** When one entity is the npc then reduce childrencount of the other object (player) by 1
                     * set witch on return and calculate a new path to her home position
                     */
                } else if (e instanceof Witch && entity instanceof Player && game.DRAMATIC) {
                    Witch witch = (Witch) e;
                    Player player = (Player) entity;
                    if (player.getChildrenCount() <= 0) return;

                    if (player.getProtectedTicks() > 0) {
                        System.out.println("NO COLLISION WEGEN PROTECTION!!");
                        return;
                    }

                    Sound.playChild();

                    witch.setOnReturn(true);
                    //new PathWorker(witch, witch.getEntityPos(), new Point((int)(witch.getHomeX() / Tile.TILE_SIZE), (int)(witch.getHomeY() / Tile.TILE_SIZE)), this).start();

                    /**
                     * set a protection against further collisions for 50 ticks ( currently 1 s) to avoid multiple collisions at once
                     */
                    player.setChildrenCount(player.getChildrenCount() - 1);
                    player.setProtectedTicks(50);

                    /**
                     * if the gameMode is currently a network game then transmit the collision to the other player
                     */
                    if (game.getGameMode() == Game.GameMode.REMOTE) {
                        ((NetworkController) game.getGameController()).changeGameStateObject(witch, Event.EventType.COLLISION);
                    }
                }

                if (e instanceof Witch && !game.DRAMATIC) {
                    size = 0;
                }

                /**
                 * undo the last movement step in X or Y- direction if collision(s) occurred
                 */
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

    /**
     * check if the given player object collide with the inside walls of an house
     *
     * @param player
     * @param o
     * @return true = the player collided - false = no collision occurred
     */
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

                        (player.getEntityPosWithCurrency(-.33).x == o_x - 1 && e_y >= o_y && e_y <= o_y + o_height) ||
                        (player.getEntityPosWithCurrency(+.33).x == o_x + o_width && e_y >= o_y && e_y <= o_y + o_height)

        );
    }
}

