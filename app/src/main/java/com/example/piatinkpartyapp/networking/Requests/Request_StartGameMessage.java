package com.example.piatinkpartyapp.networking.Requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.networking.IPackets;

public class Request_StartGameMessage implements IPackets {
    public Request_StartGameMessage() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Request_StartGameMessage) {
            return true;
        } else {
            return false;
        }
    }
}
