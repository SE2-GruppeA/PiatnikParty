package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.CardValue;

public class Response_SendSchlagToAllPlayers {
    public CardValue schlag;

    public Response_SendSchlagToAllPlayers() {
    }

    public Response_SendSchlagToAllPlayers(CardValue cardValue) {
        this.schlag = cardValue;
    }
}
