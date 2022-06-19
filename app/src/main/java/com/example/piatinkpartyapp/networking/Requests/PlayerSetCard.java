package com.example.piatinkpartyapp.networking.Requests;

import androidx.annotation.Nullable;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.networking.IPackets;

public class PlayerSetCard implements IPackets {
    public int playerID;
    public Card card;

    public PlayerSetCard() {
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public PlayerSetCard(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PlayerSetCard) {
            PlayerSetCard comp = (PlayerSetCard) obj;
            if (playerID == comp.playerID && card.equals(comp.card)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
