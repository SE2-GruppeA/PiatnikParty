package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SchnopsnDeckTest {


    /*
    @Test
    public void testGetTrump() {

        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        assertEquals(gameName, schnopsnDeck.getGameName());
        assertEquals(players, schnopsnDeck.getPlayers());

        Symbol trump = Symbol.randomSymbol();
        assertEquals(trump, schnopsnDeck.getTrump());
    }

     @Test
    public void testSelectTrump() {

    }

    @Test
    public void testSchnopsnGame() {

        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        assertEquals(new SchnopsnDeck(gameName, players).getGameName(), gameName);
        assertEquals(new SchnopsnDeck(gameName, players).getPlayers(), players);
        assertEquals(new SchnopsnDeck(GameName.Schnopsn, 3), schnopsnDeck);
    }

    */

    /*
    @Test
    public void testCardPoints() {

        GameName gameName = GameName.Schnopsn;
        int players = 3;

        HashMap<CardValue, Integer> point_map = new HashMap<>();

        CardValue cardValue = CardValue.ZEHN;
        point_map.put(cardValue, 10);

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        assertEquals(10, schnopsnDeck.cardPoints(cardValue));
    }
    */


    /*
    @Test
    public void testSwappingCards() {

        CardValue cardValue = CardValue.ZEHN;
        Symbol symbol = Symbol.KARO;
        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

    }

     */

    @Test
    void testGetSchnopsnCardValues() {

        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        ArrayList<CardValue> cardValues = schnopsnDeck.getSchnopsnCardValues();

        assertEquals(cardValues.size(), 5);
    }

    @Test
    void testCreateCards() {

        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        ArrayList<Card> cards = schnopsnDeck.getCards();

        ArrayList<Card> cardArrayList = new ArrayList<>();

        //Card card = new Card(Symbol.KARO, CardValue.ZEHN);

        for (Card card : cards) {
            cardArrayList.add(card);
        }

        assertEquals(cardArrayList.size(), cards.size());
        assertEquals(cardArrayList, cards);
    }

    @Test
    void testSwappingCard() {
        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);
        assertNotNull(schnopsnDeck.swappingCard());
    }

    @Test
    void testCardPoints() {
        CardValue cardValue = CardValue.ZEHN;
        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);
        Integer points = 10;
        assertEquals(points, schnopsnDeck.cardPoints(cardValue));
    }

    @Test
    void testCardPoints1() {
        GameName gameName = GameName.Schnopsn;
        int players = 3;

        SchnopsnDeck schnopsnDeck = new SchnopsnDeck(gameName, players);

        assertNotNull(schnopsnDeck.cardPoints());
    }
}
