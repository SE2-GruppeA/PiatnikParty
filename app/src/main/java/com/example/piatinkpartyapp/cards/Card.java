package com.example.piatinkpartyapp.cards;

import androidx.annotation.Nullable;

/*Card objects needed for game play
* each card is defined by a symbol & card value
* strings for front & backside define the picture that should be shown per card
* boolean cheated for future check if the played card is a cheat-card*/
public class Card {
    public Symbol symbol;
    public CardValue cardValue;
    public String backSide;
    public String frontSide;
    public Boolean cheated;

    public Card() {
    }

    public Card(Symbol symbol, CardValue cardValue){
        this.symbol = symbol;
        this.cardValue = cardValue;
        this.backSide = "backside";
        this.frontSide = symbol + "_" + cardValue;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Card){
            Card comp = (Card) obj;
            if (symbol == comp.symbol && cardValue == comp.cardValue && backSide.equals(comp.backSide) &&
            frontSide.equals(comp.frontSide) && cheated == comp.cheated){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
