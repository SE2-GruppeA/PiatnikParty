package com.example.piatinkpartyapp.networking.responses;

public class responseConnectedSuccessfully {
    public int playerID;
    public boolean isConnected;

    public responseConnectedSuccessfully() {
    }

    public responseConnectedSuccessfully(int playerID, boolean isConnected) {
        this.playerID = playerID;
        this.isConnected = isConnected;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
