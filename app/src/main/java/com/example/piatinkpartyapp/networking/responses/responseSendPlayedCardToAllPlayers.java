package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.Card;

public class responseSendPlayedCardToAllPlayers {
    public int playerID;
    public Card card;

    public responseSendPlayedCardToAllPlayers() {
    }

    public responseSendPlayedCardToAllPlayers(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
