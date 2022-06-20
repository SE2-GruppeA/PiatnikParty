package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.Card;

public class responsePlayerGetHandoutCard {
    public int playerID;
    public Card card;

    public responsePlayerGetHandoutCard() {
    }

    public responsePlayerGetHandoutCard(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
