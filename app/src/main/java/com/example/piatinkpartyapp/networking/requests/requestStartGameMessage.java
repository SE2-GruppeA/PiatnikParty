package com.example.piatinkpartyapp.networking.requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.networking.IPackets;

public class requestStartGameMessage implements IPackets {
    public requestStartGameMessage() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof requestStartGameMessage;
    }
}
