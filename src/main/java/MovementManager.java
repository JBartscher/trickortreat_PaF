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

public class MovementManager implements EventHandler<InputEvent> {


    Player player1;
    Player player2;

    Player inputAWSD;
    Player inputARROW;
    Player inputMOUSE;

    private Map map;


    // There are three different kinds of movement types
    public enum MovementType {
        KEYBOARD_AWSD, KEYBOARD_ARROW, MOUSE;
    }

    // represents the move directions to animate the entities
    public enum MoveDirection {
        DOWN, LEFT, RIGHT, UP
    }

    // Lokaler Multiplayer
    public MovementManager(Map map, Player player1, Player player2)
    {
        this.map = map;
        this.player1 = player1;
        this.player2 = player2;

        // register the player to movement strategies
        registerPlayerInputs(player1);
        registerPlayerInputs(player2);

    }

    // Es existiert nur ein Spieler - Remote GAME
    public MovementManager(Map map, Player player1){
        registerPlayerInputs(player1);
    }


    public void registerPlayerInputs(Player player)
    {
        if(player.movementType == MovementType.KEYBOARD_AWSD){
            inputAWSD = player;

        } else if(player.movementType == MovementType.KEYBOARD_ARROW) {
            inputARROW = player;

        } else {
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
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            System.out.println("SCENE X: " +  event.getSceneX() + " SCENE Y: " + event.getSceneY());
            System.out.println("SPIELER X: " + inputMOUSE.getxPos() + " - SPIELER Y : " + inputMOUSE.getyPos());


            if(inputMOUSE != null) {

                double currentRenderX = inputMOUSE.xPos - inputMOUSE.getxOffSet();
                double currentRenderY = inputMOUSE.yPos - inputMOUSE.getyOffSet() + Window.HEIGHT * 0.1;

                inputMOUSE.setTarget(inputMOUSE.getxPos() + (event.getSceneX() - currentRenderX), inputMOUSE.getyPos() + (event.getSceneY() - currentRenderY));
            }
        }
    }

    public void handleKeyboard(KeyEvent event)
    {
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
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
        entity.move();


        double movementSize = entity.getSpeed() / Game.FRAMES;
        double moveX = 0.0;
        double moveY = 0.0;

        if( (entity.target.x - entity.xPos) > 2) {
            moveX = movementSize;
            entity.setMoveDirection(MoveDirection.RIGHT);
        } else if (entity.target.x < entity.xPos) {
            moveX = - movementSize;
            entity.setMoveDirection(MoveDirection.LEFT);
        }

        if( (entity.target.y - entity.yPos) > 2) {
            moveY = movementSize;
            entity.setMoveDirection(MoveDirection.DOWN);
        } else if (entity.target.y < entity.yPos) {
            moveY = -movementSize;
            entity.setMoveDirection(MoveDirection.UP);
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

        entity.setEntityImage();

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
            if (map.getMap()[entity.getEntityPos().y][entity.getEntityPos().x].isDoorTile()) {

                System.out.println("COLLIDE WITH DOOR!");

                for (MapObject obj : map.getMapSector().getAllContainingMapObjects()) {
                    try {
                        House h = (House) obj;
                        if (h.intersects(p)) {
                            if(entity instanceof Player)
                            h.visit((Player)entity);
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
        }
    }


    /**
     * checks if the new player position would be out of bounds
     *
     * @return true if out of bounds failing which false
     */
    private boolean outOfBounds(Entity entity) {
        Placeable p = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);
        // mapSector does not contain player anymore
        return !map.getMapSector().intersects(p);
    }
}

