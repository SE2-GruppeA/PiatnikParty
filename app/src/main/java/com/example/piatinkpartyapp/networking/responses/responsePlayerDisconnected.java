package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class responsePlayerDisconnected implements IPackets {
    public int playerID;

    public responsePlayerDisconnected() {
    }

    public responsePlayerDisconnected(int playerID) {
        this.playerID = playerID;
    }
}
