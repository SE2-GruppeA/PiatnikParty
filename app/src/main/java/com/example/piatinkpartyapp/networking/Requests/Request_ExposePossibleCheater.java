package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class Request_ExposePossibleCheater implements IPackets {
    public Integer playerId;

    public Request_ExposePossibleCheater() {

    }

    public Request_ExposePossibleCheater(Integer playerId) {
        this.playerId = playerId;
    }
}
