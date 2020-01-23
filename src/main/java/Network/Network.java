package main.java.Network;

/**
 * every class that has the ability to communicate within a network implements this interface
 */
public interface Network {

    public void communicate();

    /**
     * update the gamestate of an network engine
     * @param gameState
     */
    public void setGameState(GameState gameState);

}
