package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Card;

public class Response_PlayerGetHandoutCard {
    public int playerID;
    public Card card;

    public Response_PlayerGetHandoutCard() {
    }

    public Response_PlayerGetHandoutCard(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
