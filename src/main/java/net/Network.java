package main.java.net;

/**
 * every class that has the ability to communicate within a network implements this interface
 */
public interface Network {

    void communicate();

    /**
     * update the gamestate of an network engine
     * @param gameState
     */
    void setGameState(GameState gameState);


    public GameState getGameState();

}
