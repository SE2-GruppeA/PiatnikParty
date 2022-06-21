package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.SchnopsnGame;
import com.example.piatinkpartyapp.gamelogic.WattnGame;


import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


/*
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WattnTest {

    @Test
     void constructorTest(){
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame game = new WattnGame(lobby);
        Player p1 = game.lobby.getPlayerByID(1);
        Player p2 = game.lobby.getPlayerByID(2);
        game.setRoundStartPlayer(p1);
        assertEquals(p1, game.getRoundStartPlayer());
    }
    @Test
     void resetWattnDeckTest(){
        Game game = new Game();
        Lobby lobby = game.lobby;
        WattnGame wattnGame = new WattnGame(lobby);
        wattnGame.resetWattnDeck(2);
        assertNotNull(wattnGame.deck);
    }

   @Test
    public void startWattnGameTest(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");

       WattnGame game = new WattnGame(lobby);
       Player player1 = game.lobby.getPlayerByID(1);
       Player player2 = game.lobby.getPlayerByID(2);

       game.resetRoundFinished();

       game.resetPlayerPoints();

       game.resetCheating();
       game.resetWattnDeck(2);

       ArrayList<Card> handCards1 = game.deck.getHandCards();
       player1.setHandcards(handCards1);

       ArrayList<Card> handCards2 = game.deck.getHandCards();
       player2.setHandcards(handCards2);

       game.setRoundStartPlayer(player1);

       Assert.assertEquals(player1.getPoints(), 0);
       Assert.assertEquals(player2.getPoints(), 0);
       Assert.assertEquals(player1.isCheaten(), false);
       Assert.assertEquals(player2.isCheaten(), false);
       Assert.assertNotNull(player1.getHandcards());
       Assert.assertNotNull(player2.getHandcards());


    }
    @Test
    void getNextPlayerTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        WattnGame game = new WattnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Player next = game.getNextPlayer(player1);

        Assert.assertEquals(player2, next);
    }
    @Test
    void checkIfAllPlayersFinishedRoundTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        WattnGame game = new WattnGame(lobby);
        Player player1 = game.lobby.getPlayerByID(1);
        Player player2 = game.lobby.getPlayerByID(2);

        game.setRoundStartPlayer(player1);

        Card card1 = new Card(Symbol.HERZ, CardValue.ASS);
        player1.setCardPlayed(card1);
        player1.setRoundFinished(true);

        Boolean finished = game.checkIfAllPlayersFinishedRound();

        Assert.assertEquals(finished, false);
        Assert.assertNotNull(finished);
    }
    @Test
    void checkIfAllPlayersFinishedRoundTest2() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        WattnGame game = new WattnGame(lobby);
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

        Assert.assertEquals(finished, true);
        Assert.assertNotNull(finished);
    }
   /* @Test
    public void setHitsetTrumptest() {
        Lobby lobby = new Lobby();
        WattnGame wg = new WattnGame(lobby);
        wg.startGameWattn();
        wg.setSchlag(CardValue.ASS);
        wg.setTrump(Symbol.HERZ);
        assertEquals(CardValue.ZEHN, wg.deck.getHit());
        assertEquals(Symbol.HERZ, wg.deck.getTrump());
        // assertEquals(Symbol.HERZ, wg.deck.rightCard.symbol);
        //assertEquals(CardValue.ZEHN,wg.deck.rightCard.cardValue);
    }*/


    @Test
    public void testGetWattnWinnerRightCard() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame wg = new WattnGame(lobby);
        Player player1 = wg.lobby.getPlayerByID(1);
        Player player2 = wg.lobby.getPlayerByID(2);

        wg.setRoundStartPlayer(player1);
        wg.resetWattnDeck(2);
        wg.deck.setHit(CardValue.ACHT);
        wg.deck.setTrump(Symbol.KREUZ);
        Card card1 = new Card(wg.deck.getTrump(), wg.deck.getHit());
        player1.setCardPlayed(card1);
        Card card2 = new Card(Symbol.HERZ, CardValue.SIEBEN);
        player2.setCardPlayed(card2);


        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));


        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }

    @Test
    public void testGetWattnWinnerRightCard2(){
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame wg = new WattnGame(lobby);
        Player player1 = wg.lobby.getPlayerByID(1);
        Player player2 = wg.lobby.getPlayerByID(2);

        wg.setRoundStartPlayer(player1);
        wg.resetWattnDeck(2);
        wg.deck.setTrump(Symbol.PICK);
        wg.deck.setHit(CardValue.KOENIG);

        wg.setRoundStartPlayer(player1);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        player1.setCardPlayed(new Card(Symbol.PICK, CardValue.KOENIG));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }
   @Test
    public void testGetRoundWinnerWattnHit1(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");
       WattnGame wg = new WattnGame(lobby);
       Player player1 = wg.lobby.getPlayerByID(1);
       Player player2 = wg.lobby.getPlayerByID(2);

       wg.setRoundStartPlayer(player1);
       wg.resetWattnDeck(2);
       wg.deck.setTrump(Symbol.PICK);
       wg.deck.setHit(CardValue.KOENIG);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player1.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        player2.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }

   @Test
    public void testGetRoundWinnerWattnHit2(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");
       WattnGame wg = new WattnGame(lobby);
       Player player1 = wg.lobby.getPlayerByID(1);
       Player player2 = wg.lobby.getPlayerByID(2);

       wg.setRoundStartPlayer(player1);
       wg.resetWattnDeck(2);
       wg.deck.setTrump(Symbol.PICK);
       wg.deck.setHit(CardValue.KOENIG);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        player1.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player2, winner);

    }
    @Test
    public void testGetRoundWinnerWattnTrump1(){
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame wg = new WattnGame(lobby);
        Player player1 = wg.lobby.getPlayerByID(1);
        Player player2 = wg.lobby.getPlayerByID(2);

        wg.setRoundStartPlayer(player1);
        wg.resetWattnDeck(2);
        wg.deck.setTrump(Symbol.PICK);
        wg.deck.setHit(CardValue.KOENIG);
        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.PICK,CardValue.ACHT));
        player1.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }
   @Test
    public void testGetRoundWinnerWattnTrump2(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");
       WattnGame wg = new WattnGame(lobby);
       Player player1 = wg.lobby.getPlayerByID(1);
       Player player2 = wg.lobby.getPlayerByID(2);

       wg.setRoundStartPlayer(player1);
       wg.resetWattnDeck(2);
       wg.deck.setTrump(Symbol.PICK);
       wg.deck.setHit(CardValue.KOENIG);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.PICK,CardValue.ASS));
        player1.setCardPlayed(new Card(Symbol.PICK, CardValue.OBER));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player2, winner);

    }
   @Test
    public void testGetRoundWinnerWattnOneTrump1(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");
       WattnGame wg = new WattnGame(lobby);
       Player player1 = wg.lobby.getPlayerByID(1);
       Player player2 = wg.lobby.getPlayerByID(2);

       wg.setRoundStartPlayer(player1);
       wg.resetWattnDeck(2);
       wg.deck.setTrump(Symbol.PICK);
       wg.deck.setHit(CardValue.KOENIG);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        player1.setCardPlayed(new Card(Symbol.PICK, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }
    @Test
    public void testwithPlayersOneTrump2(){
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame wg = new WattnGame(lobby);
        Player player1 = wg.lobby.getPlayerByID(1);
        Player player2 = wg.lobby.getPlayerByID(2);

        wg.setRoundStartPlayer(player1);
        wg.resetWattnDeck(2);
        wg.deck.setTrump(Symbol.PICK);
        wg.deck.setHit(CardValue.KOENIG);

        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player1.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        player2.setCardPlayed(new Card(Symbol.PICK, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player2, winner);

    }
    @Test
    public void testGetRoundWinnerWattnHigherCard1(){
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");
        WattnGame wg = new WattnGame(lobby);
        Player player1 = wg.lobby.getPlayerByID(1);
        Player player2 = wg.lobby.getPlayerByID(2);

        wg.setRoundStartPlayer(player1);
        wg.resetWattnDeck(2);
        wg.deck.setTrump(Symbol.PICK);
        wg.deck.setHit(CardValue.KOENIG);
        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player1.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        player2.setCardPlayed(new Card(Symbol.KREUZ, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player1, winner);

    }
   @Test
    public void testGetRoundWinnerWattnHigherCard2(){
       Lobby lobby = new Lobby();
       lobby.addPlayer(1, "Player 1");
       lobby.addPlayer(2, "Player 2");
       WattnGame wg = new WattnGame(lobby);
       Player player1 = wg.lobby.getPlayerByID(1);
       Player player2 = wg.lobby.getPlayerByID(2);

       wg.setRoundStartPlayer(player1);
       wg.resetWattnDeck(2);
       wg.deck.setTrump(Symbol.PICK);
       wg.deck.setHit(CardValue.KOENIG);
        assertEquals(player1, wg.roundStartPlayer);
        assertEquals(player2, wg.getNextPlayer(player1));

        player2.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        player1.setCardPlayed(new Card(Symbol.KREUZ, CardValue.UNTER));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(player2, winner);

    }
}
