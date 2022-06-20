package com.example.piatinkpartyapp.cardsTests;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class WattnDeckTest {

    private WattnDeck deck;
    private GameName gameName;
    private Card card;

    @BeforeEach
    void init(){
        int players = 1;
        gameName = GameName.Wattn;
        card = new Card();
        deck = new WattnDeck(gameName,players);
    }

    @AfterEach
    void clear(){
        deck = null;
        gameName = null;
        card = null;
    }

    @Test
    void testLastCard(){
        //Testing if not the last card in deck
        deck.lastCard();
        assertNotNull(deck);
    }

    @Test
    void testGetRightCard(){

        //Testing if not the highest Value
        card.cardValue = CardValue.ACHT;
        card.symbol = Symbol.HERZ;

        assertNull(deck.getRightCard());
    }

    @Test
    void testCardPoints(){
        Card SIEBEN = new Card();
        Card ACHT = new Card();
        Card NEUN = new Card();
        Card ZEHN = new Card();
        Card UNTER = new Card();
        Card OBER = new Card();
        Card KOENIG = new Card();
        Card ASS = new Card();

        SIEBEN.cardValue = CardValue.SIEBEN;
        ACHT.cardValue = CardValue.ACHT;
        NEUN.cardValue = CardValue.NEUN;
        ZEHN.cardValue = CardValue.ZEHN;
        UNTER.cardValue = CardValue.UNTER;
        OBER.cardValue = CardValue.OBER;
        KOENIG.cardValue = CardValue.KOENIG;
        ASS.cardValue = CardValue.ASS;

        deck.cardPoints(SIEBEN.cardValue);
        assertEquals(1,deck.cardPoints(SIEBEN.cardValue));
        assertEquals(2,deck.cardPoints(ACHT.cardValue));
        assertEquals(3,deck.cardPoints(NEUN.cardValue));
        assertEquals(4,deck.cardPoints(ZEHN.cardValue));
        assertEquals(5,deck.cardPoints(UNTER.cardValue));
        assertEquals(6,deck.cardPoints(OBER.cardValue));
        assertEquals(7,deck.cardPoints(KOENIG.cardValue));
        assertEquals(8,deck.cardPoints(ASS.cardValue));
    }

}
