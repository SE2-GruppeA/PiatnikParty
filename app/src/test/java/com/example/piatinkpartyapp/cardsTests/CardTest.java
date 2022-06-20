package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void testCardValue() {
        Symbol symbol = Symbol.KARO;
        CardValue cardValue = CardValue.ASS;

    	assertEquals(Symbol.KARO, symbol);
    	assertEquals(CardValue.ASS, cardValue);
    }

    @Test
    void testIsCheated() {
    	Card card = new Card(Symbol.KARO, CardValue.ASS);
    	assertEquals(false, card.isCheated());
    }

    @Test
    void testSetCheated() {
        Card card = new Card(Symbol.KARO, CardValue.ASS);
        Boolean cheated = true;
        card.setCheated(cheated);
        assertEquals(true, card.isCheated());
    }

    @Test
    void testCard() {
        Card card = new Card(Symbol.KARO, CardValue.ASS);
        assertEquals(Symbol.KARO, card.getSymbol());
    }

    @Test
    void testGetBackSide() {
        Card card = new Card(Symbol.KARO, CardValue.ASS);
        assertEquals(new Card(Symbol.KARO, CardValue.ASS).backSide, card.getBackSide());
    }

    @Test
    void testGetFrontSide() {
        Card card = new Card(Symbol.KARO, CardValue.ASS);
        assertEquals(new Card(Symbol.KARO, CardValue.ASS).frontSide, card.getFrontSide());
    }
}
