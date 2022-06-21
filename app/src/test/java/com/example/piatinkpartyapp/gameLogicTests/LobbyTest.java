package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LobbyTest {

    private Lobby lobby;
    private Player playerTest;

    @BeforeEach
     void init(){
        //Initialize Variable
        lobby = new Lobby();

    }

    @AfterEach
     void breakDown(){

        lobby = null;
    }

    @Test
     void testWinnerGameOfVoting(){
        GameName SCHNOPSN = GameName.Schnopsn;

        lobby.getWinnerGameOfVoting();
        assertEquals(SCHNOPSN,lobby.getWinnerGameOfVoting());

    }


 /*   @Test
     void testHandleVotingForNextGame(){
        int playerID = 1;
        GameName SCHNOPSN = GameName.Schnopsn;
        lobby.handleVotingForNextGame(playerID,SCHNOPSN);

        assertNotNull(lobby.currentGame);
    }*/

    @Test
     void testCheckIfAllPlayersFinishedVoting(){
        lobby.checkIfAllPlayersFinishedVoting();
        assertTrue(lobby.checkIfAllPlayersFinishedVoting());
    }

    @Test
     void test(){
        assertNotNull(lobby.getPlayerByID(1));
    }

    @Test
    void removePlayerTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        lobby.removePlayer(lobby.getPlayerByID(1));

        assertEquals(lobby.getPlayers().get(0).getPlayerName(), "Player 2");
    }

    @Test
    void resetVotingFinishedTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        lobby.getPlayerByID(1).setVotingFinished(true);

        assertEquals(lobby.getPlayerByID(1).isVotingFinished(), true);

        lobby.resetVotingFinished();

        assertEquals(lobby.getPlayerByID(1).isVotingFinished(), false);
    }

    @Test
    void checkIfAllPlayersFinishedVotingTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        lobby.getPlayerByID(1).setVotingFinished(true);

        assertEquals(lobby.checkIfAllPlayersFinishedVoting(), false);

        lobby.getPlayerByID(2).setVotingFinished(true);

        assertEquals(lobby.checkIfAllPlayersFinishedVoting(), true);
    }

    @Test
    void handleVotingForNextGameTest() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(1, "Player 1");
        lobby.addPlayer(2, "Player 2");

        lobby.handleVotingForNextGame(1, GameName.Schnopsn);

        lobby.getPlayerByID(1).setVotingGame(GameName.Schnopsn);
        assertEquals(lobby.getPlayerByID(1).getVotingGame(), GameName.Schnopsn);
    }
}
