package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class playerDisconnected implements IPackets {
    public int playerID;

    public playerDisconnected() {
    }

    public playerDisconnected(int playerID) {
        this.playerID = playerID;
    }
}
