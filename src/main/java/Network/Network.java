package main.java.Network;

public interface Network {

    public void communicate();

    // wird aufgerufen auf ein NetworkEngine-Objekt durch den NetworkController - aktualisiert GameState
    public void setGameState(GameState gameState);

}
