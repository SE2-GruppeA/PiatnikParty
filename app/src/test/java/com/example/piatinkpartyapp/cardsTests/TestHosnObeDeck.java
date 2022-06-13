package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.HosnObeDeck;
import com.example.piatinkpartyapp.cards.Symbol;

import org.junit.jupiter.api.Test;

public class TestHosnObeDeck {

    @Test
    public void testConstructor() {
        GameName gameName = null;
        int players = 0;
        HosnObeDeck hosnObeDeck = new HosnObeDeck(gameName, players);
        assertNotNull(hosnObeDeck);
    }

    @Test
    public void testCardPoints() {
        GameName gameName = GameName.HosnObe;
        int players = 4;
        CardValue cardValue = CardValue.ACHT;
        HosnObeDeck hosnObeDeck = new HosnObeDeck(gameName, players);
        assertEquals(8, hosnObeDeck.cardPoints(cardValue));
    }

}
