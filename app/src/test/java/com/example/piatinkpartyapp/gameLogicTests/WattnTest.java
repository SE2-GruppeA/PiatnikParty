package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.WattnGame;


import org.junit.jupiter.api.Test;



/*
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WattnTest {

    @Test
     void constructorTest(){
        Game game = new Game();
        Lobby lobby = game.lobby;
        WattnGame wattnGame = new WattnGame(lobby);
        assertNotNull(wattnGame);
    }
    @Test
     void resetWattnDeckTest(){
        Game game = new Game();
        Lobby lobby = game.lobby;
        WattnGame wattnGame = new WattnGame(lobby);
        wattnGame.resetWattnDeck(2);
        assertNotNull(wattnGame.deck);
    }
  /*  @Test
    public void startWattnGameTest(){
        Game game = new Game();
        Lobby lobby = game.lobby;
        WattnGame wattnGame = new WattnGame(lobby);
        wattnGame.startGameWattn();


    }*/


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

 /*   @Test
    public void getRightCard() {
        Lobby lobby = new Lobby();
        WattnGame wg = new WattnGame(lobby);
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);

        lobby.addPlayer(null, "player 1");
        lobby.addPlayer(null, "player2");
        wg.setTrump(Symbol.PICK);
        assertEquals(CardValue.KOENIG, wg.deck.getHit());
        assertEquals(Symbol.PICK, wg.deck.getTrump());

        assertEquals(CardValue.KOENIG, wg.rightCard().getCardValue());
        assertEquals(Symbol.PICK, wg.rightCard().getSymbol());
    }*/

   /* @Test
    public void testWithPlayersRightCard() {
        Lobby lobby = new Lobby();
        WattnGame wg = new WattnGame(lobby);
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);

        lobby.addPlayer(null, "player 1");
        lobby.addPlayer(null, "player2");
        wg.setSchlag(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p1.setCardPlayed(new Card(Symbol.KARO, CardValue.ACHT));
        p2.setCardPlayed(new Card(Symbol.PICK, CardValue.KOENIG));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }*/

   /* @Test
    public void testwithPlayersRight2(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        p1.setCardPlayed(new Card(Symbol.PICK, CardValue.KOENIG));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p1, winner);

    }*/
   /* @Test
    public void testwithPlayersHit1(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p1.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        p2.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p1, winner);

    }*/

 /*   @Test
    public void testwithPlayersHit2(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.HERZ,CardValue.KOENIG));
        p1.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }*/
   /* @Test
    public void testwithPlayersTrump1(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.PICK,CardValue.ACHT));
        p1.setCardPlayed(new Card(Symbol.PICK, CardValue.ASS));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p1, winner);

    }*/
  /*  @Test
    public void testwithPlayersTrump2(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.PICK,CardValue.ASS));
        p1.setCardPlayed(new Card(Symbol.PICK, CardValue.OBER));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }*/
 /*   @Test
    public void testwithPlayersOneTrump1(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        p1.setCardPlayed(new Card(Symbol.PICK, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p1, winner);

    }*/
   /* @Test
    public void testwithPlayersOneTrump2(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p1.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        p2.setCardPlayed(new Card(Symbol.PICK, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }*/
   /* @Test
    public void testwithPlayersHigherCard1(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p1.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        p2.setCardPlayed(new Card(Symbol.KREUZ, CardValue.SIEBEN));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p1, winner);

    }*/
  /*  @Test
    public void testwithPlayersHigherCard2(){
        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);
        WattnGame wg = new WattnGame();
        wg.players.add(p1);
        wg.players.add(p2);
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);

        wg.roundStartPlayer = p1;

        assertEquals(p1, wg.roundStartPlayer);
        assertEquals(p2, wg.getNextPlayer(p1));

        p2.setCardPlayed(new Card(Symbol.KREUZ,CardValue.ASS));
        p1.setCardPlayed(new Card(Symbol.KREUZ, CardValue.UNTER));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }*/
}
