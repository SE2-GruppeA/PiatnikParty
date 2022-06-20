package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.Symbol;

public class responseSendTrumpToAllPlayers {
    public Symbol trump;

    public responseSendTrumpToAllPlayers() {
    }

    public responseSendTrumpToAllPlayers(Symbol symbol) {
        this.trump = symbol;
    }
}
