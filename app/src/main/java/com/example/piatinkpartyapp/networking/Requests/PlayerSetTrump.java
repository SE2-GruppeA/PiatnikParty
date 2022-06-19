package com.example.piatinkpartyapp.networking.Requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.IPackets;

public class PlayerSetTrump implements IPackets {
    public Symbol trump;

    public PlayerSetTrump() {
    }

    public PlayerSetTrump(Symbol trump) {
        this.trump = trump;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PlayerSetTrump) {
            PlayerSetTrump comp = (PlayerSetTrump) obj;
            if (trump == comp.trump) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
