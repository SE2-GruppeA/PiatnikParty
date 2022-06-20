package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.Card;

import java.util.ArrayList;

public class responseSendHandCards {
    public int playerID;
    public ArrayList<Card> cards;

    public responseSendHandCards() {
    }

    public responseSendHandCards(int playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }
}
