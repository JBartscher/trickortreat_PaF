package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.House;
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
          //  int tileNr = game.getMap().getMap()[entity.getEntityPos().y][entity.getEntityPos().x][0].getTileNr();
          //  if (tileNr >= 20 && tileNr <= 25) movementSize *= 1.3;


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
                            if(entity instanceof Player && h.isUnvisited()) {
                                h.visit((Player) entity);
                                if(game.getGameMode() == Game.GameMode.REMOTE) game.getNetworkController().changeGameStateObject(h);
                            }
                        }
                    } catch (ClassCastException ex) {
                        // the Object is not a House
                        continue;
                    }
                }
            }
            System.out.println("COLLIDE!");
            // revert movement
            entity.setyPos(entity.getyPos() - size);
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
        if (map.getMapSector().intersectsWithContainingItems(p)) {
            System.out.println("COLLIDE!");
            // revert movement
            entity.setxPos(entity.getxPos() - size);
            // TODO: LÖST die Kollision zwischen zwei Spielern
        } else {

            for(Entity e : game.getListOfAllEntites()) {
                if(e == entity) continue;

                if (Math.abs(entity.getxPos() - e.getxPos()) < 0.5 * Tile.TILE_SIZE && Math.abs(entity.getyPos() - e.getyPos()) < Tile.TILE_SIZE * 0.5 ) {
                    entity.setxPos(entity.getxPos() - size);
                    if(e instanceof AliceCooper && entity instanceof Player) {
                        ((AliceCooper)e).playSong((Player)entity);
                    }
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

        Placeable p1 = new Placeable(entity.getEntityPosWithCurrency(-0.1).y,  entity.getEntityPosWithCurrency(-0.1).x, 1, 1, 0);
        Placeable p2 = new Placeable(entity.getEntityPosWithCurrency(+0.33).y,  entity.getEntityPosWithCurrency(+0.33).x, 1, 1, 0);

        // mapSector does not contain player anymore
        return !map.getMapSector().intersects(p1) || !map.getMapSector().intersects(p2);
    }


}

