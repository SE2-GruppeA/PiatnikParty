package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.WattnGame;


import org.junit.jupiter.api.Test;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WattnTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void setHitsetTrumptest(){
        WattnGame wg = new WattnGame();
        //   wg.deck.setHit(CardValue.ZEHN);
        // wg.deck.setTrump(Symbol.HERZ);
        wg.setHit(CardValue.ZEHN);
        wg.setTrump(Symbol.HERZ);
        assertEquals(CardValue.ZEHN, wg.deck.getHit());
        assertEquals(Symbol.HERZ, wg.deck.getTrump());
        // assertEquals(Symbol.HERZ, wg.deck.rightCard.symbol);
        //assertEquals(CardValue.ZEHN,wg.deck.rightCard.cardValue);
    }

    @Test
    public void getRightCard(){
        WattnGame wg = new WattnGame();
        wg.setHit(CardValue.KOENIG);
        wg.setTrump(Symbol.PICK);
        assertEquals(CardValue.KOENIG, wg.deck.getHit());
        assertEquals(Symbol.PICK, wg.deck.getTrump());

        assertEquals(CardValue.KOENIG, wg.rightCard().getCardValue());
        assertEquals(Symbol.PICK, wg.rightCard().getSymbol());
    }

    @Test
    public void testWithPlayersRightCard(){
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

        p1.setCardPlayed(new Card(Symbol.KARO,CardValue.ACHT));
        p2.setCardPlayed(new Card(Symbol.PICK, CardValue.KOENIG));
        Player winner = wg.getRoundWinnerWattn();
        assertEquals(p2, winner);

    }
    @Test
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

    }
    @Test
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

    }

    @Test
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

    }
    @Test
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

    }
    @Test
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

    }
    @Test
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

    }
    @Test
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

    }
    @Test
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

    }
    @Test
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

    }
}
