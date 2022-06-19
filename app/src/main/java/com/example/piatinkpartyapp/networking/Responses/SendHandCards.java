package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.Card;

import java.util.ArrayList;

public class SendHandCards {
    public int playerID;
    public ArrayList<Card> cards;

    public SendHandCards() {
    }

    public SendHandCards(int playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }
}
