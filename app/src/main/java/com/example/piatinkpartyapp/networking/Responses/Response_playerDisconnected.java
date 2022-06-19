package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class Response_playerDisconnected implements IPackets {
    public int playerID;

    public Response_playerDisconnected() {
    }

    public Response_playerDisconnected(int playerID) {
        this.playerID = playerID;
    }
}
