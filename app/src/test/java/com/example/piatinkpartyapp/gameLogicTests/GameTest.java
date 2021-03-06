package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.jupiter.api.Test;

class GameTest {

    Lobby lobby;

    @Test
     void constructorTest() {
        Game game = new Game();
        GameName Schnopsn = null;
        SchnopsnDeck deck = new SchnopsnDeck(Schnopsn,4);
        assertNotNull(deck);
        assertNotNull(game);
    }

    @Test
     void setCard() {

        int playerId = 1;

        CardValue cardValue1 = CardValue.ZEHN;
        CardValue cardValue2 = CardValue.ASS;
        Symbol symbol = Symbol.KARO;
        Symbol symbol2 = Symbol.HERZ;

        Card card1 = new Card(symbol, cardValue1);
        Card card2 = new Card(symbol2, cardValue2);

        Player player = new Player();
        Player player2 = new Player();
        player.setId(playerId);
        player2.setId(playerId + 1);

        player.setCardPlayed(card1);
        player2.setCardPlayed(card2);

        assertNotEquals(player, player2);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.getCardValue(), card2.getCardValue());
    }

    @Test
     void setCardTest() {

        int playerId = 1;

        CardValue cardValue1 = CardValue.ZEHN;
        CardValue cardValue2 = CardValue.ASS;
        Symbol symbol = Symbol.KARO;
        Symbol symbol2 = Symbol.HERZ;

        Card card1 = new Card(symbol, cardValue1);
        Card card2 = new Card(symbol2, cardValue2);

        Player player = new Player();
        Player player2 = new Player();
        player.setId(playerId);
        player2.setId(playerId + 1);

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
     void getRoundWinnerPlayerSchnopsnTest() {

        Game game = new Game();

        Player winnerPlayer = new Player();
        Player currentPlayer = new Player();

        winnerPlayer.setId(1);
        currentPlayer.setId(2);

        assertNotEquals(winnerPlayer.getId(), currentPlayer.getId());

        winnerPlayer.setCardPlayed(new Card(Symbol.KARO, CardValue.ZEHN));
        currentPlayer.setCardPlayed(new Card(Symbol.KARO, CardValue.ASS));

        assertNotNull(winnerPlayer.getCardPlayed());
        assertNotNull(currentPlayer.getCardPlayed());
    }

    @Test
     void testConstructor() {
        Game game = new Game();
        assertNotNull(game);
    }

    @Test
     void testGetLobby() {
        Game game = new Game();
        assertEquals(game.lobby, game.getLobby());
    }

    @Test
    void testSendPlayerCardToAllPlayers() {
        int playerId = 1;

        Symbol symbol = Symbol.HERZ;
        CardValue cardValue = CardValue.OBER;

        Card card = new Card(symbol, cardValue);
    }

    @Test
    void testGivePlayerBestCard() {
        Game game = new Game();
        int playerId = 1;
    }

    @Test
    void testSetLobby() {
        Lobby lobby2 = new Lobby();
        this.lobby = lobby2;
        assertNotNull(lobby);
    }

    /*
    @Test
    void testResetPlayedCard() {

        int playerId = 1;
        String playerName = "Player1";

        Player player = new Player(playerId, playerName);

        CardValue cardValue = CardValue.OBER;
        Symbol symbol = Symbol.HERZ;

        Card card = new Card(symbol, cardValue);

        Game game = new Game();
        player.setCardPlayed(card);

        assertNotNull(card);

        game.resetPlayedCard();
        assertNull(card);
    }

     */

    @Test
    void testIsPlayerCheater() {
        int playerId = 1;
        int exposerId = 2;

        Lobby lobby = new Lobby();
        lobby.addPlayer(playerId, "Player1");
        lobby.addPlayer(exposerId, "Player2");

        Game game = new Game();
        assertFalse(game.isPlayerCheater(playerId, exposerId));
    }

    @Test
    void testGetRandomPlayer() {
        Game game = new Game();
        Lobby lobby = new Lobby();
        int player = 1;
        lobby.addPlayer(player, "Player");
        assertNull(game.getRandomPlayer());
    }

    @Test
    void testSetTrump() {
        Game game = new Game();
        assertNull(game.getTrump());
    }

    @Test
    void testSetMainPlayerId() {

        Integer newMainPlayerId = 2;
        Game game = new Game();

        newMainPlayerId = 3;
        game.setMainPlayerId(newMainPlayerId);

        assertEquals(3, game.getMainPlayerId());
    }

    @Test
    void testGetMainPlayerId() {
        Integer mainPlayerId = 1;
        Game game = new Game();
        game.setMainPlayerId(mainPlayerId);
        assertEquals(1, game.getMainPlayerId());
    }
}
