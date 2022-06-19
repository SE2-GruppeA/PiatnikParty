package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class IsCheater implements IPackets {
    public boolean isCheater;

    public IsCheater() {
    }

    public IsCheater(boolean isCheater) {
        this.isCheater = isCheater;
    }
}
