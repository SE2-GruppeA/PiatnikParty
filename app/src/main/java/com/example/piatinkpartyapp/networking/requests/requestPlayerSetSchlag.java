package com.example.piatinkpartyapp.networking.requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.networking.IPackets;

public class requestPlayerSetSchlag implements IPackets {
    public CardValue schlag;

    public requestPlayerSetSchlag() {
    }

    public requestPlayerSetSchlag(CardValue schlag) {
        this.schlag = schlag;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof requestPlayerSetSchlag) {
            requestPlayerSetSchlag comp = (requestPlayerSetSchlag) obj;
            return schlag == comp.schlag;
        } else {
            return false;
        }
    }
}
