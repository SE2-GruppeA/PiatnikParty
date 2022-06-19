package com.example.piatinkpartyapp.networking.Requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.networking.IPackets;

public class StartGameMessage implements IPackets {
    public StartGameMessage() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof StartGameMessage) {
            return true;
        } else {
            return false;
        }
    }
}
