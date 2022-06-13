package com.example.piatinkpartyapp.gameLogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LobbyTest {

    private Lobby lobby;
    private Player playerTest;

    @BeforeEach
    public void init(){
        //Initialize Variable
        lobby = new Lobby();

    }

    @AfterEach
    public void breakDown(){

        lobby = null;
    }

    @Test
    public void testWinnerGameOfVoting(){
        GameName SCHNOPSN = GameName.Schnopsn;

        lobby.getWinnerGameOfVoting();
        assertEquals(SCHNOPSN,lobby.getWinnerGameOfVoting());

    }

    @Test
    public void testHandleVotingForNextGame(){
        int playerID = 1;
        GameName SCHNOPSN = GameName.Schnopsn;
        lobby.handleVotingForNextGame(playerID,SCHNOPSN);

        assertNotNull(lobby.currentGame);
    }

    @Test
    public void testCheckIfAllPlayersFinishedVoting(){
        lobby.checkIfAllPlayersFinishedVoting();
        assertTrue(lobby.checkIfAllPlayersFinishedVoting());
    }

    @Test
    public void test(){
        assertNotNull(lobby.getPlayerByID(1));
    }
}