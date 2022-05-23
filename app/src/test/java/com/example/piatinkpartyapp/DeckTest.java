package com.example.piatinkpartyapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.cards.Deck;
import com.example.piatinkpartyapp.cards.GameName;

import org.junit.jupiter.api.Test;

public class DeckTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGetHandCards() {

        GameName gameName = GameName.Schnopsn;
        int players = 2;

        Deck deck = new Deck(gameName, players);
        deck.getHandCards();
    }

    @Test
    public void testTakeCards() {

        GameName gameName = GameName.Schnopsn;
        int players = 2;

        Deck deck = new Deck(gameName, players);

    }
}
