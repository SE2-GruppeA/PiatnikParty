package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.SchnopsnGame;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SchnopsnTest {

    @Test
    public void constructorTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        assertNotNull(game.getDeck());
        assertNotNull(game);
    }

    @Test
    public void setRoundStartPlayerTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        assertEquals(player1, game.roundStartPlayer);
    }

    @Test
    public void getWinnerPlayerSchnopsnTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(game.getDeck().getTrump(), CardValue.ASS);
        player1.setCardPlayed(card1);

        Card card2 = new Card(game.getDeck().getTrump(), CardValue.UNTER);
        player2.setCardPlayed(card2);

        Player winner = game.getRoundWinnerPlayerSchnopsn();

        assertEquals(player1, winner);
    }

    @Test
    public void getWinnerPlayerSchnopsnTest2() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(game.getDeck().getTrump(), CardValue.ASS);
        player1.setCardPlayed(card1);

        Card card2 = new Card(Symbol.HERZ, CardValue.UNTER);
        player2.setCardPlayed(card2);

        Player winner = game.getRoundWinnerPlayerSchnopsn();

        assertEquals(player1, winner);
    }

    @Test
    public void getWinnerPlayerSchnopsnTest3() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);

        Card card2 = new Card(Symbol.HERZ, CardValue.UNTER);
        player2.setCardPlayed(card2);

        Player winner = game.getRoundWinnerPlayerSchnopsn();

        assertEquals(player1, winner);
    }

    @Test
    public void resetRoundFinishedTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(game.getDeck().getTrump(), CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Card card2 = new Card(game.getDeck().getTrump(), CardValue.UNTER);
        player2.setCardPlayed(card2);
        player2.setRoundFinished(true);

        assertEquals(player1.isRoundFinished(), true);
        assertEquals(player2.isRoundFinished(), true);
    }

    @Test
    public void sendHandCardsTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        ArrayList<Card> handCards1 = game.getDeck().getHandCards();
        player1.setHandcards(handCards1);

        ArrayList<Card> handCards2 = game.getDeck().getHandCards();
        player2.setHandcards(handCards2);

        assertNotNull(player1.getHandcards());
        assertNotNull(player2.getHandcards());
    }

    @Test
    public void startGameTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.resetRoundFinished();

        game.resetPlayerPoints();

        game.resetCheating();

        ArrayList<Card> handCards1 = game.getDeck().getHandCards();
        player1.setHandcards(handCards1);

        ArrayList<Card> handCards2 = game.getDeck().getHandCards();
        player2.setHandcards(handCards2);

        game.setRoundStartPlayer(player1);

        assertEquals(player1.getPoints(), 0);
        assertEquals(player2.getPoints(), 0);
        assertEquals(player1.isCheaten(), false);
        assertEquals(player2.isCheaten(), false);

    }

    @Test
    public void getNextPlayerTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Player next = game.getNextPlayer(player1);

        assertEquals(player2, next);
    }

    @Test
    public void checkIfAllPlayersFinishedRoundTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Boolean finished = game.checkIfAllPlayersFinishedRound();

        assertEquals(finished, false);
        assertNotNull(finished);
    }

    @Test
    public void checkIfAllPlayersFinishedRoundTest2() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Card card2 = new Card(Symbol.HERZ, CardValue.NEUN);
        player2.setCardPlayed(card2);
        player2.setRoundFinished(true);

        Boolean finished = game.checkIfAllPlayersFinishedRound();

        assertEquals(finished, true);
        assertNotNull(finished);
    }

    @Test
    public void setDeckTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        SchnopsnDeck deck = new SchnopsnDeck(GameName.Schnopsn, 2);
        game.setDeck(deck);

        assertEquals(game.getDeck(), deck);
        assertNotNull(game.getDeck());
    }

    @Test
    public void mixCardsTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        SchnopsnGame game = new SchnopsnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        SchnopsnDeck deck = new SchnopsnDeck(GameName.Schnopsn, 2);
        game.setDeck(deck);

        assertEquals(game.getDeck(), deck);
        assertNotNull(game.getDeck());
    }
/*
    @Test
    public void startSchnopsnTest() {
        GameServer server = GameServer.getInstance();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 */
}
