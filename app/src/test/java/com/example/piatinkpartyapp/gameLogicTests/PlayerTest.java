package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.jupiter.api.Test;

public class PlayerTest {

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
}
