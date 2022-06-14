package com.example.piatinkpartyapp.cards;

import java.util.ArrayList;

public class HosnObeDeck extends Deck {

    public Symbol trump;
    public CardValue hit;
    public Card highestCard;

    public HosnObeDeck(GameName gameName, int players) {
        super(gameName, players);
    }

    public int cardPoints(CardValue cardValue) {
        int points = 0;
        switch (cardValue) {
            case SIEBEN:
                points = 7;
                break;
            case ACHT:
                points = 8;
                break;
            case NEUN:
                points = 9;
                break;
            case ZEHN:
            case UNTER:
            case OBER:
            case KOENIG:
                points = 10;
                break;
            case ASS:
                points = 11;
                break;
            default:
                points = 0;
                break;
        }
        return points;
    }

    public void setHit(CardValue hit) {
        this.hit = hit;
    }

    public void setHighestCard(Card highestCard) {
        this.highestCard = highestCard;
    }

    public void setTrump(Symbol trump) {
        this.trump = trump;
    }

    public Card getHighestCard() {

        for (Card card : deck) {
            if(card.cardValue == getHit() && card.symbol == getTrump()){
                return card;
            }
        }

        return null;
    }

    public CardValue getHit() {
        return hit;
    }

    public Symbol getTrump() {
        return trump;
    }


}
