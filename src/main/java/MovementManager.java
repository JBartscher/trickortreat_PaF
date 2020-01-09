package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.Network.Event;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.Mansion;
import main.java.map.Map;
import main.java.map.MapObject;
import main.java.map.Placeable;
import main.java.map.Tile;

import java.io.Serializable;

public class MovementManager implements EventHandler<InputEvent>, Serializable {

    Player player1;
    Player player2;

    Player inputAWSD;
    Player inputARROW;
    Player inputMOUSE;

    private Map map;
    private Game game;


    // There are three different kinds of movement types
    public enum MovementType implements Serializable {
        KEYBOARD_AWSD, KEYBOARD_ARROW, MOUSE;
    }

    // represents the move directions to animate the entities
    public enum MoveDirection implements Serializable {
        DOWN, LEFT, RIGHT, UP
    }

    // Lokaler Multiplayer
    public MovementManager(Game game, Player player1, Player player2)
    {
        this.game = game;
        this.map = game.getMap();
        this.player1 = player1;
        this.player2 = player2;

        // register the player to movement strategies
        registerPlayerInputs(player1);
        registerPlayerInputs(player2);

    }


    public void registerPlayerInputs(Player player)
    {
        if(player.movementType == MovementType.KEYBOARD_AWSD){
            inputAWSD = player;

        } else if(player.movementType == MovementType.KEYBOARD_ARROW) {
            inputARROW = player;

        } else if(player.movementType == MovementType.MOUSE) {
            inputMOUSE = player;
        }
    }

    @Override
    public void handle(InputEvent event) {

        if(event.getClass() == MouseEvent.class) {
            handleMouse((MouseEvent)event);
        } else if (event.getClass() == KeyEvent.class) {
            handleKeyboard((KeyEvent)event);
        }
    }

    public void handleMouse(MouseEvent event)
    {
        if(inputMOUSE == null) return;
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {


            if(inputMOUSE != null) {

                double currentRenderX = inputMOUSE.xPos - inputMOUSE.getxOffSet() + Tile.TILE_SIZE / 2;
                double currentRenderY = inputMOUSE.yPos - inputMOUSE.getyOffSet() + Window.HEIGHT * 0.1 + Tile.TILE_SIZE / 2;
                if(inputMOUSE == game.getOtherPlayer()) currentRenderX += Game.WIDTH + 2 * Tile.TILE_SIZE;

                inputMOUSE.setTarget(inputMOUSE.getxPos() + (event.getSceneX() - currentRenderX), inputMOUSE.getyPos() + (event.getSceneY() - currentRenderY));
            }
        }
    }

    public void handleKeyboard(KeyEvent event)
    {
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            if(event.getCode() == KeyCode.M) {
                Sound.muteSound();
            }

            if(event.getCode() == KeyCode.P) {
                if(game.getNetworkController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                    game.getNetworkController().changeGameStateObject("PAUSED", Event.EventType.PAUSED);
                    game.paused = true;
                } else if (game.getGameMode() == Game.GameMode.LOCAL) {
                    game.paused = true;
                }

            }


            if(event.getCode() == KeyCode.A) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.target.x - inputAWSD.speed * 100, inputAWSD.target.y );
                }
            }

            if(event.getCode() == KeyCode.W) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.target.x, inputAWSD.target.y - inputAWSD.speed * 100 );
                }
            }

            if(event.getCode() == KeyCode.S) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.target.x, inputAWSD.target.y + inputAWSD.speed * 100 );
                }
            }

            if(event.getCode() == KeyCode.D) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.target.x + inputAWSD.speed * 100, inputAWSD.target.y);
                }
            }



        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {

            if(event.getCode() == KeyCode.R) {
                game.paused = false;
                if(game.getNetworkController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                    game.getNetworkController().changeGameStateObject("UNPAUSED", Event.EventType.UNPAUSED);
                }
            }

            if(event.getCode() == KeyCode.A) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.xPos, inputAWSD.target.y );
                }
            }

            if(event.getCode() == KeyCode.W) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.xPos, inputAWSD.yPos );
                }
            }

            if(event.getCode() == KeyCode.S) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.xPos, inputAWSD.yPos );
                }
            }

            if(event.getCode() == KeyCode.D) {
                if(inputAWSD != null) {
                    inputAWSD.setTarget( inputAWSD.xPos, inputAWSD.target.y );
                }
            }
        }


        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            if(event.getCode() == KeyCode.LEFT) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.target.x - inputARROW.speed * 100, inputARROW.target.y );
                }
            }

            if(event.getCode() == KeyCode.UP) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.target.x, inputARROW.target.y - inputARROW.speed * 100 );
                }
            }

            if(event.getCode() == KeyCode.DOWN) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.target.x, inputARROW.target.y + inputARROW.speed * 100 );
                }
            }

            if(event.getCode() == KeyCode.RIGHT) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.target.x + inputARROW.speed * 100, inputARROW.target.y);
                }
            }


        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if(event.getCode() == KeyCode.LEFT) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.xPos, inputARROW.target.y );
                }
            }

            if(event.getCode() == KeyCode.UP) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.xPos, inputARROW.yPos );
                }
            }

            if(event.getCode() == KeyCode.DOWN) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.xPos, inputARROW.yPos );
                }
            }

            if(event.getCode() == KeyCode.RIGHT) {
                if(inputARROW != null) {
                    inputARROW.setTarget( inputARROW.xPos, inputARROW.target.y );
                }
            }
        }
    }

    // move-method - uses speed from entity class and Game FRAMES
    public void moveObject(Entity entity) {

        double movementSize = entity.getSpeed() / Game.FRAMES;

        // TODO: FUNKTIONIERT ÜBER NETZWERK WEGEN NULLPOINTER NICHT
            //int tileNr = game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][0].getTileNr();
            // if (tileNr >= 20 && tileNr <= 25) movementSize *= 1.3;


        double moveX = 0.0;
        double moveY = 0.0;

        if( (entity.target.x - entity.xPos) >= movementSize) {
            moveX = movementSize;
            entity.setMoveDirection(MoveDirection.RIGHT);
        } else if ( (entity.xPos >= entity.target.x + movementSize)) {
            moveX = - movementSize;
            entity.setMoveDirection(MoveDirection.LEFT);
        } else {

        }

        if( (entity.target.y - entity.yPos) > movementSize) {
            moveY = movementSize;
            entity.setMoveDirection(MoveDirection.DOWN);
        } else if (entity.yPos >= entity.target.y + movementSize) {
            moveY = -movementSize;
            entity.setMoveDirection(MoveDirection.UP);
        } else {

        }


        // check out of bounds and change entity position
        entity.setxPos(entity.getxPos() + moveX);
        if(outOfBounds(entity)) {
            entity.setxPos(entity.getxPos() - moveX);
        }

        entity.setyPos(entity.getyPos() + moveY);
        if(outOfBounds(entity)) {
            entity.setyPos(entity.getyPos() - moveY);
        }

        // checks for collisions and events
        moveHorizontal(moveX, entity);
        moveVertical(moveY, entity);

        entity.setEntityImage(false);
    }


    /**
     *
     * move entity objects in vertical direction
     * checks for collisions with houses and doors
     */
    public void moveVertical(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);

        // check collisions in vertical direction
        if (map.getMapSector().intersectsWithContainingItems(p)) {
            // collision with door
            if (map.getMap()[entity.getEntityPos().y][entity.getEntityPos().x][1].isDoorTile()) {

                System.out.println("COLLIDE WITH DOOR!");

                for (MapObject obj : map.getMapSector().getAllContainingMapObjects()) {
                    try {
                        House h = (House) obj;
                        if (h.intersects(p)) {
                            if ( (entity instanceof Player && h.isUnvisited() || (entity instanceof Player && obj instanceof Mansion && entity == ((Mansion)h).insidePlayer))) {
                                h.visit((Player) entity);

                                if(game.getGameMode() == Game.GameMode.REMOTE) {
                                    game.getNetworkController().changeGameStateObject(h, Event.EventType.VISITED);
                                }
                            }
                        }
                    } catch (ClassCastException ex) {
                        // the Object is not a House
                        continue;
                    }
                }
            }

            // revert movement when entity is not a player and has a collision detection
            if  (entity instanceof Player && ((Player) entity).isNoCollision()) {

            } else {
                System.out.println("COLLIDE!");
                entity.setyPos(entity.getyPos() - size);
            }


            // TODO: LÖST die Kollision zwischen zwei Spielern, muss noch auf alle Entitäten erweitert werden
        } else {

            for(Entity e : game.getListOfAllEntites()) {
                if(e == entity) continue;

                if (Math.abs(entity.getxPos() - e.getxPos()) < 0.5 * Tile.TILE_SIZE && Math.abs(entity.getyPos() - e.getyPos()) < Tile.TILE_SIZE * 0.5 ) {
                    entity.setyPos(entity.getyPos() - size);
                    if(e instanceof AliceCooper && entity instanceof Player) {
                        ((AliceCooper)e).playSong((Player)entity);
                    }
                }
            }
        }
    }

    /**
     *
     * move entity objects in horizontal direction
     * checks for collisions with houses and doors
     */
    public void moveHorizontal(double size, Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);
        if (map.getMapSector().intersectsWithContainingItems(p) && !entity.isNoCollision()) {


            // revert movement when entity is not a player and has a collision detection
            if  (entity instanceof Player && ((Player) entity).isNoCollision()) {

            } else {
                System.out.println("COLLIDE!");
                entity.setxPos(entity.getxPos() - size);
            }


            // TODO: LÖST die Kollision zwischen zwei Spielern
        } else {

            // überprüft die Kollision zwischen Entitäten
            checkCollisionsBetweenEntities(entity, size);

        }
    }


    public void checkCollisionsBetweenEntities(Entity entity, double size) {

        for(Entity e : game.getListOfAllEntites()) {
            if(e == entity) continue;

            if (Math.abs(entity.getxPos() - e.getxPos()) < 0.5 * Tile.TILE_SIZE && Math.abs(entity.getyPos() - e.getyPos()) < Tile.TILE_SIZE * 0.5 ) {
                entity.setxPos(entity.getxPos() - size);
                if(e instanceof AliceCooper && entity instanceof Player) {
                    ((AliceCooper)e).playSong((Player)entity);
                } else if(e instanceof Witch && entity instanceof Player) {
                    System.out.println("KOLLIDIERT MIT HEXE!");
                    ((Player)entity).setChildrenCount(1);
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

        if(entity instanceof Player && ((Player)entity).isInside()) {
            Player player = (Player)entity;
            return outOfBoundsInside((Player)entity, player.getInsideObject());

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
                (e_x >= o_x && e_x <= o_x + o_width && e_y == o_y - 1) ||
                        (e_x >= o_x && e_x <= o_x + o_width && player.getEntityPosWithCurrency(+0.5).y == o_y + o_height) ||

                        (player.getEntityPosWithCurrency( -.1).x == o_x - 1 && e_y >= o_y && e_y <= o_y + o_height) ||
                        (player.getEntityPosWithCurrency(+.33).x == o_x + o_width && e_y >= o_y && e_y <= o_y + o_height)

        );

    }


}

