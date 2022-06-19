package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Symbol;

public class SendTrumpToAllPlayers {
    public Symbol trump;

    public SendTrumpToAllPlayers() {
    }

    public SendTrumpToAllPlayers(Symbol symbol) {
        this.trump = symbol;
    }
}
