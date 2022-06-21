package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PlayerTest {

  @Test
     void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
     void testPlayerName() {
        Player player = new Player();
        player.setPlayerName("John");
        assertEquals("John", player.getPlayerName());
    }

    @Test
     void testPlayerScore() {
        Player player = new Player();
        player.setPlayerName("John");
        assertEquals(0, player.getPoints());
    }

    @Test
    void testPlayer() {
        Player player = new Player(1, "Player 1");

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        Card card2 = new Card(Symbol.KARO , CardValue.ZEHN);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        player.addHandcard(card1);
        player.addHandcard(card2);
        assertEquals(cards, player.getHandcards());

        player.removeHandcard(card2);
        player.removeHandcard(card1);
        assertEquals(null, player.getHandcards());

        player.addPoints(2);
        player.addPointsScoreboard(4);

        assertEquals(player.getPoints(), 2);
        assertEquals(player.getPointsScoreboard(), 4);

        player.resetPoints();
        player.resetPointsScoreboard();

        assertEquals(player.getPoints(), 0);
        assertEquals(player.getPointsScoreboard(), 0);

        player.setPoints(6);
        assertEquals(player.getPoints(), 6);

        player.setFinished(true);
        assertEquals(player.isFinished(), true);



    }
}
