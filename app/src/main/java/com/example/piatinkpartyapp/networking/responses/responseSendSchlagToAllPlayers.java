package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.CardValue;

public class responseSendSchlagToAllPlayers {
    public CardValue schlag;

    public responseSendSchlagToAllPlayers() {
    }

    public responseSendSchlagToAllPlayers(CardValue cardValue) {
        this.schlag = cardValue;
    }
}
