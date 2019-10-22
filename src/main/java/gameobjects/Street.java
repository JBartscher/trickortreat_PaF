package gameobjects;

import exceptions.NotEnoughStreetConnections;

/**
 * Class which represents a Street Object
 */
public class Street {
    private boolean isConectedUp;
    private boolean isConectedDown;
    private boolean isConectedLeft;
    private boolean isConectedRight;

    private int connections;

    public Street(boolean up, boolean down, boolean right, boolean left) throws NotEnoughStreetConnections {
        isConectedUp = up;
        isConectedDown = down;
        isConectedRight = right;
        isConectedLeft = left;
        //calc how many connections the street has
        if (up)
            connections++;
        if (down)
            connections++;
        if (right)
            connections++;
        if (left)
            connections++;
        if (connections < 1)
            throw new NotEnoughStreetConnections("an street Object has to be connected to at least one other street Object!");
    }

    private StreetPice calculateStreetPice() {
        //TODO: calc the right street pice
        return StreetPice.CROSSING;
    }

}

/**
 * Enumeration for all possible street pieces
 */
enum StreetPice {
    CROSSING,
    T_CROSSING_L_T_R, //left top right
    T_CROSSING_L_B_R, //left bottom right
    T_CROSSING_T_L_B, //top left bottom
    T_CROSSING_T_R_B, //top right bottom
    TURN_L_T,
    TURN_L_B,
    TURN_R_T,
    TURN_R_B,
    LINE_H, //horizontal
    LINE_V //vertical
}