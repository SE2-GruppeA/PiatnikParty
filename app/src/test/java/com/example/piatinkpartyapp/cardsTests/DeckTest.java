package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Deck;
import com.example.piatinkpartyapp.cards.GameName;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
    private GameName gameName;
    private int players;
    private Deck deck;

    @BeforeEach
    public void init(){
        gameName = GameName.Schnopsn;
        players = 2;
        deck = new Deck(gameName, players);
    }

    @AfterEach
    public void cleaer(){
        gameName = null;
        deck = null;
    }

    @Test
    public void testCreateCards(){
        //Testing if the deck is created
        deck.createCards();
        assertNotNull(deck);
    }

    @Test
    public void testGetHandCards() {
        assertNotNull(deck.getHandCards());
    }

    @Test
    public void testTakeCard() {
        // Testing if a card is taken from the deck
        assertNotEquals(deck.takeCard(),deck.takeCard());

    }
}
