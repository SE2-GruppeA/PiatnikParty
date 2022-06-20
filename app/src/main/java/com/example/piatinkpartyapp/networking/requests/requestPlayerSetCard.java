package com.example.piatinkpartyapp.networking.requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.networking.IPackets;

public class requestPlayerSetCard implements IPackets {
    public int playerID;
    public Card card;

    public requestPlayerSetCard() {
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public requestPlayerSetCard(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof requestPlayerSetCard) {
            requestPlayerSetCard comp = (requestPlayerSetCard) obj;
            return playerID == comp.playerID && card.equals(comp.card);
        } else {
            return false;
        }
    }
}
