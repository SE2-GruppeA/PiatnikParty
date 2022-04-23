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
        this.cards = createCards();
        this.deck = mixCards();

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
    /*mixing order of cards in the deck
    * checking functionality via logcat since order of deck cards isnt visile in antoher way*/
    public ArrayList<Card> mixCards(){
        Log.d("b4############",cards.toString());
        Collections.shuffle(cards);
        Log.d("after############",cards.toString());
        return cards;
    }
    /*each player can request handcards --> array of cards is returned
    * in basic case 5 cards special cases handled by inheritance with separate decks*/
    public ArrayList<Card> getHandCards(){
        ArrayList<Card> handCards = new ArrayList<>();
        /*card is taken from deck & added to handcard arraylist, needs to be removed from deck to prevent duplicate cards*/
        for(int i= 0; i<5; i++){
            Card c = deck.get(i);
            handCards.add(c);
            deck.remove(c);
        }
        return handCards;
    }
}
