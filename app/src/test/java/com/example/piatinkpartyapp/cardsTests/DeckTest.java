package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Deck;
import com.example.piatinkpartyapp.cards.GameName;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeckTest {
    private GameName gameName;
    private Deck deck;

    @BeforeEach
    void init(){
        gameName = GameName.Schnopsn;
        int players = 2;
        deck = new Deck(gameName, players);
    }

    @AfterEach
    void cleaer(){
        gameName = null;
        deck = null;
    }

    @Test
    void testCreateCards(){
        //Testing if the deck is created
        deck.createCards();
        assertNotNull(deck);
    }

    @Test
    void testGetHandCards() {
        assertNotNull(deck.getHandCards());
    }

    @Test
    void testTakeCard() {
        // Testing if a card is taken from the deck
        assertNotEquals(deck.takeCard(),deck.takeCard());

    }
}
