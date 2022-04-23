package com.example.piatinkpartyapp.cards;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/*Deck (Stapel) which provides cards to players
* depended on game --> inheritance & number of players
* contains cards*/
public class Deck {
    GameName gameName;
    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<Card> deck;
    int players;

    public Deck(GameName gameName, int players){
        this.gameName = gameName;
        this.players = players;

    }
    /*creating cards of each symbol & card-value
    * to be contained in the deck later*/
    public ArrayList<Card> createCards(){
        for(Symbol s :Symbol.values()){
            for(CardValue v: CardValue.values()){
                Card c = new Card(s,v);
                cards.add(c);
            }
        }
        return cards;
    }
}
