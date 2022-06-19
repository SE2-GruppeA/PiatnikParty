package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Card;

public class SendPlayedCardToAllPlayers {
    public int playerID;
    public Card card;

    public SendPlayedCardToAllPlayers() {
    }

    public SendPlayedCardToAllPlayers(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }
}
