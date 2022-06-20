package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Symbol;

public class Response_SendTrumpToAllPlayers {
    public Symbol trump;

    public Response_SendTrumpToAllPlayers() {
    }

    public Response_SendTrumpToAllPlayers(Symbol symbol) {
        this.trump = symbol;
    }
}
