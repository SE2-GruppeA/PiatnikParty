package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Player;


import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class GameTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void constructorTest() {
        Game game = new Game();
        SchnopsnDeck deck;
    }

    public void setCard() {

        int playerId = 1;

        CardValue cardValue1 = CardValue.ZEHN;
        CardValue cardValue2 = CardValue.ASS;
        Symbol symbol = Symbol.KARO;
        Symbol symbol2 = Symbol.HERZ;

        Card card1 = new Card(symbol, cardValue1);
        Card card2 = new Card(symbol2, cardValue2);

        Game game = new Game();
        Player player = new Player();
        Player player2 = new Player();
        player.setId(playerId);
        player2.setId(playerId + 1);

        game.lobby.players.add(player);
        game.lobby.players.add(player2);

        player.setCardPlayed(card1);
        player2.setCardPlayed(card2);

        assertNotEquals(player, player2);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.getCardValue(), card2.getCardValue());
    }

    @Test
    public void setCardTest() {

        int playerId = 1;

        CardValue cardValue1 = CardValue.ZEHN;
        CardValue cardValue2 = CardValue.ASS;
        Symbol symbol = Symbol.KARO;
        Symbol symbol2 = Symbol.HERZ;

        Card card1 = new Card(symbol, cardValue1);
        Card card2 = new Card(symbol2, cardValue2);

        Game game = new Game();
        Player player = new Player();
        Player player2 = new Player();
        player.setId(playerId);
        player2.setId(playerId + 1);

        game.lobby.players.add(player);
        game.lobby.players.add(player2);

        player.setCardPlayed(card1);
        player2.setCardPlayed(card2);

        assertNotEquals(player, player2);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.getCardValue(), card2.getCardValue());

        assertEquals(player.getCardPlayed(), card1);

        assertEquals(card1, player.getCardPlayed());
        assertEquals(card2, player2.getCardPlayed());
    }

    @Test
    public void getRoundWinnerPlayerSchnopsnTest() {

        Game game = new Game();

        Player winnerPlayer = new Player();
        Player currentPlayer = new Player();

        winnerPlayer.setId(1);
        currentPlayer.setId(2);

        game.lobby.players.add(winnerPlayer);
        game.lobby.players.add(currentPlayer);

        assertNotEquals(winnerPlayer.getId(), currentPlayer.getId());

        winnerPlayer.setCardPlayed(new Card(Symbol.KARO, CardValue.ZEHN));
        currentPlayer.setCardPlayed(new Card(Symbol.KARO, CardValue.ASS));

        assertNotNull(winnerPlayer.getCardPlayed());
        assertNotNull(currentPlayer.getCardPlayed());
    }


}
