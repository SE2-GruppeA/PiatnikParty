package com.example.piatinkpartyapp.cards;
/*Card objects needed for game play
* each card is defined by a symbol & card value
* strings for front & backside define the picture that should be shown per card
* boolean cheated for future check if the played card is a cheat-card*/
public class Card {
    Symbol symbol;
    CardValue cardValue;
    String backSide;
    String frontSide;
    Boolean cheated;

    public Card(Symbol symbol, CardValue cardValue){
        this.symbol = symbol;
        this.cardValue = cardValue;
        this.backSide = "backside";
        this.frontSide = symbol+"_"+cardValue;
        this.cheated = false;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public String getBackSide() {
        return backSide;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public Boolean isCheated() {
        return cheated;
    }

    public void setCheated(Boolean cheated) {
        this.cheated = cheated;
    }
}
