package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class responseIsCheater implements IPackets {
    public boolean isCheater;

    public responseIsCheater() {
    }

    public responseIsCheater(boolean isCheater) {
        this.isCheater = isCheater;
    }
}
