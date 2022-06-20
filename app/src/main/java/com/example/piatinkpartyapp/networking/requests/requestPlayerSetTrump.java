package com.example.piatinkpartyapp.networking.requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.IPackets;

public class requestPlayerSetTrump implements IPackets {
    public Symbol trump;

    public requestPlayerSetTrump() {
    }

    public requestPlayerSetTrump(Symbol trump) {
        this.trump = trump;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof requestPlayerSetTrump) {
            requestPlayerSetTrump comp = (requestPlayerSetTrump) obj;
            return trump == comp.trump;
        } else {
            return false;
        }
    }
}
