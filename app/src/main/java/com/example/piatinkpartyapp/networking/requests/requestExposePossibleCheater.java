package com.example.piatinkpartyapp.networking.requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class requestExposePossibleCheater implements IPackets {
    public Integer playerId;

    public requestExposePossibleCheater() {

    }

    public requestExposePossibleCheater(Integer playerId) {
        this.playerId = playerId;
    }
}
