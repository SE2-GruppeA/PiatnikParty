package com.example.piatinkpartyapp.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchnopsnDeck extends Deck {

    /*specialization of card-deck tailored for schnopsn
    * only 20 cards from less cardvalues
    * selected trump symbol
    * map of points of each cardValue for easier point / win calculation*/
    Symbol trump;
    HashMap<CardValue, Integer> pointMap;
    ArrayList<CardValue> schnopsnCardValues;

    public SchnopsnDeck(GameName gameName, int players) {
        super(gameName, players);
        this.trump = selectTrump();
    }

    /* exclude cardvalues 7 to 9 */
    public ArrayList<CardValue> getSchnopsnCardValues(){
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

    /*get game points per cardvalue*/
    public Integer cardPoints(CardValue v) {
        Integer points = 0;
        switch (v) {
            case ZEHN:
                points = 10;
                break;
            case UNTER:
                points = 2;
                break;
            case OBER:
                points = 3;
                break;
            case KOENIG:
                points = 4;
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

    /*creation of full cardvalue point mapping*/
    public Map<CardValue, Integer> cardPoints() {
        pointMap = new HashMap<>();
        Integer points = 0;
        for (CardValue v : schnopsnCardValues) {
            points = cardPoints(v);
            pointMap.put(v, points);
        }

        return pointMap;
    }

    /*in schopsn there is a swapping card showing its image half under the deck, its symbol is trump & it can be swapped against the trump UNTER during the game*/
    public Card swappingCard(){
        Card swappingCard = null;
        CardValue v = CardValue.randomValue();
        // as the random symbol might not be part of the schnopsnCardValue arraylist it needs to be check if it is part
        if(schnopsnCardValues.contains(v)){
            for(Card c : deck){
                if(c.cardValue == v && c.symbol == trump){
                    swappingCard = c;
                    deck.remove(c);
                    return swappingCard;
                }
            }

        } else { // if it isnt part, the function is called recurseivly again
            return swappingCard();
        }
        return  null;
    }


    public HashMap<CardValue, Integer> getPointMap() {
        return pointMap;
    }


    public void setPointMap(HashMap<CardValue, Integer> pointMap) {
        this.pointMap = pointMap;
    }

    public void setSchnopsnCardValues(ArrayList<CardValue> schnopsnCardValues) {
        this.schnopsnCardValues = schnopsnCardValues;
    }

    public void setTrump(Symbol trump) {
        this.trump = trump;
    }
}

