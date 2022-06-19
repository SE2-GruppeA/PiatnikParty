package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Card;

public class Response_SendPlayedCardToAllPlayers {
    public int playerID;
    public Card card;

    public Response_SendPlayedCardToAllPlayers() {
    }

    public Response_SendPlayedCardToAllPlayers(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
