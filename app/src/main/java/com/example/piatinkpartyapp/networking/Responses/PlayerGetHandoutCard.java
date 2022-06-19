package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Card;

public class PlayerGetHandoutCard {
    public int playerID;
    public Card card;

    public PlayerGetHandoutCard() {
    }

    public PlayerGetHandoutCard(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
