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
        if(deck.size() >=1){
            return deck.get(deck.size() - 1);
        }
        return  null;
    }
}
