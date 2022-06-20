package com.example.piatinkpartyapp.networking.Requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.networking.IPackets;

public class Request_PlayerSetSchlag implements IPackets {
    public CardValue schlag;

    public Request_PlayerSetSchlag() {
    }

    public Request_PlayerSetSchlag(CardValue schlag) {
        this.schlag = schlag;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Request_PlayerSetSchlag) {
            Request_PlayerSetSchlag comp = (Request_PlayerSetSchlag) obj;
            if (schlag == comp.schlag) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
