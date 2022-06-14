package com.example.piatinkpartyapp.cards;

public class WattnDeck extends Deck {
    /*specialized Deck for "wattn"
     * has a player selected "schlog = hit"
     * and a player selected trump
     * all cards contained
     * last card of the deck is shown
     * players only get 5 hand cards at the beginning, no cards are drawn from the deck*/
    public Symbol trump;
    public CardValue hit;
    public Card rightCard;

    public WattnDeck(GameName gameName, int players) {
        super(gameName, players);
    }

    public Symbol getTrump() {
        return trump;
    }

    public void setTrump(Symbol trump) {
        this.trump = trump;
    }

    public CardValue getHit() {
        return hit;
    }

    public void setHit(CardValue hit) {
        this.hit = hit;
    }

    public Card lastCard(){
        if(deck.size() >=1) {
            return deck.get(deck.size() - 1);
        }
        return  null;
    }

    public Card getRightCard(){
        for (Card c : deck) {
            if(c.cardValue == getHit() && c.symbol == getTrump()){
                return c;
            }
        }
        return null;
    }

    public Integer cardPoints(CardValue v){
        Integer points = 0;
        if(v.equals(getHit())){
            points = 100;
        }else{
            switch (v){
                case SIEBEN:
                    points = 1;
                    break;
                case ACHT:
                    points = 2;
                    break;
                case NEUN:
                    points = 3;
                    break;
                case ZEHN:
                    points = 4;
                    break;
                case UNTER:
                    points = 5;
                    break;
                case OBER:
                    points = 6;
                    break;
                case KOENIG:
                    points = 7;
                    break;
                case ASS:
                    points =8;
                    break;
                default:
                    points = 0;
                    break;
            }
        }
        return points;
    }
}
