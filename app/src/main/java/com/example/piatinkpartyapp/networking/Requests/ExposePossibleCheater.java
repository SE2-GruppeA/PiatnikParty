package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class ExposePossibleCheater implements IPackets {
    public Integer playerId;

    public ExposePossibleCheater() {

    }

    public ExposePossibleCheater(Integer playerId) {
        this.playerId = playerId;
    }
}
