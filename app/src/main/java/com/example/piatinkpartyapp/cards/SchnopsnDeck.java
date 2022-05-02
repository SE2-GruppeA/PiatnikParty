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

    public SchnopsnDeck(GameName gameName, int players) {
        super(gameName, players);
        this.trump = selectTrump();
    }
    /*get game points per cardvalue*/
    private Integer cardPoints(CardValue v) {
        Integer points = 0;
        switch (v.name()) {
            case ("ZEHN"):
                points = 10;
                break;
            case ("UNTER"):
                points = 2;
                break;
            case ("OBER"):
                points = 3;
                break;
            case ("KOENIG"):
                points = 4;
                break;
            case ("ASS"):
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
        point_map = new HashMap<CardValue, Integer>();
        Integer points = 0;
        for (CardValue v : schnopsnCardValues) {
            points = cardPoints(v);
            point_map.put(v, points);
        }

        return point_map;
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

        }else { // if it isnt part, the function is called recurseivly again
            return swappingCard();
        }
        return  null;
    }
}

