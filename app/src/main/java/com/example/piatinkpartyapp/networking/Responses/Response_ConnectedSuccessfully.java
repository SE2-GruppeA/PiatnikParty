package com.example.piatinkpartyapp.networking.Responses;

public class Response_ConnectedSuccessfully {
    public int playerID;
    public boolean isConnected;

    public Response_ConnectedSuccessfully() {
    }

    public Response_ConnectedSuccessfully(int playerID, boolean isConnected) {
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
