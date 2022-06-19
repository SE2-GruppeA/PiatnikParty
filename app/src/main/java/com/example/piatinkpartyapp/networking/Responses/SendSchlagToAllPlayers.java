package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.CardValue;

public class SendSchlagToAllPlayers {
    public CardValue schlag;

    public SendSchlagToAllPlayers() {
    }

    public SendSchlagToAllPlayers(CardValue cardValue) {
        this.schlag = cardValue;
    }
}
