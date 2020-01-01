package main.java.Network;

import main.java.GameState;

public class NetworkController {

    public enum NetworkRole {
        SERVER, CLIENT
    }

    private NetworkRole networkRole;

    // repräsentiert eine Instanz, die das Interface Network implementiert (communicate-method)
    // ClientEngine und ServerEngine implementieren das Interface
    private Network networkEngine;
    private GameState gameState;

    public NetworkController(Network networkEngine, NetworkRole networkRole) {
        this.networkEngine = networkEngine;
        this.networkRole = networkRole;
    }


    public NetworkRole getNetworkRole() {
        return networkRole;
    }

    public void setNetworkRole(NetworkRole networkRole) {
        this.networkRole = networkRole;
    }

    public Network getNetworkEngine() {
        return networkEngine;
    }

    public void setNetworkEngine(Network networkEngine) {
        this.networkEngine = networkEngine;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void communicate() {
        networkEngine.communicate();
    }
}
