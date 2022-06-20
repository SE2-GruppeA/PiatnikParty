package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.SchnopsnGame;

import org.junit.Test;

public class SchnopsnTest {
   /*
    @Test
    public void constructorTest2() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(null, "Player 1");
        lobby.addPlayer(null, "Player 2");

        Game game = new SchnopsnGame(lobby);
        SchnopsnDeck deck = new SchnopsnDeck(GameName.Schnopsn,4);
        assertNotNull(deck);
        assertNotNull(game.getDeck());
    }

    @Test
    public void constructorTest() {
        Game game = new Game();
        GameName Schnopsn = null;
        SchnopsnDeck deck = new SchnopsnDeck(Schnopsn,4);
        assertNotNull(deck);
        assertNotNull(game);
    }

    @Test
    public void getWinnerPlayerSchnopsnTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(null, "Player 1");
        lobby.addPlayer(null, "Player 2");

        lobby.getPlayers().get(0).setCardPlayed(new Card(Symbol.HERZ, CardValue.ASS));
        lobby.getPlayers().get(1).setCardPlayed(new Card(Symbol.HERZ, CardValue.UNTER));

        lobby.currentGame = new SchnopsnGame(lobby);
        lobby.currentGame.setRoundStartPlayer(lobby.getPlayers().get(0));
        Player winner = lobby.currentGame.getRoundWinnerPlayerSchnopsn();

        assertEquals( lobby.getPlayers().get(0), winner);
    }

    */
}
