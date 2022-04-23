package com.example.piatinkpartyapp.cards;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchnopsnDeck extends Deck{
    /*specialization of card-deck tailored for schnopsn
    * only 20 cards from less cardvalues
    * selected trump symbol
    * map of points of each cardValue for easier point / win calculation*/
    Symbol trump;
    HashMap<CardValue, Integer> point_map;
    ArrayList<CardValue> schnopsnCardValues;

    /*exclude cardvalues 7 to 9*/
    private ArrayList<CardValue> getSchnopsnCardValues(){
        schnopsnCardValues = new ArrayList<>();
        for(CardValue v : CardValue.values()){
            if( v!= CardValue.SIEBEN && v!= CardValue.ACHT && v != CardValue.NEUN){
                schnopsnCardValues.add(v);
            }
        }
        return schnopsnCardValues;
    }

    @Override
    public ArrayList<Card> createCards(){
        for(Symbol s : Symbol.values()){
            for( CardValue v : getSchnopsnCardValues()){
                cards.add(new Card(s,v));
            }
        }
        return cards;
    }
    public Symbol getTrump() {
        return trump;
    }

    public Symbol selectTrump() {
        return Symbol.randomSymbol();
    }

}

