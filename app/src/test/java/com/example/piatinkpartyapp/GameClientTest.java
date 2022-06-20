
package com.example.piatinkpartyapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import androidx.lifecycle.LiveData;

import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;

import org.junit.jupiter.api.Test;

class GameClientTest {

    String ip = "127.0.0.1";
    GameServer gameServer = new GameServer();
    GameClient gameClient;
    @Test
    void testIfClientCanConnect() {
        try{
            gameClient = new GameClient(ip);

            Thread.sleep(2000);

            LiveData<Boolean> connectionState = gameClient.getConnectionState();
            assertEquals(true, connectionState.getValue());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testIfGameStarted() {
        try{
            gameClient = new GameClient(ip);
            gameServer.startNewGameServer();

            Thread.sleep(2000);

            LiveData<Boolean> isGameStartedState = gameClient.isGameStarted();
            assertEquals(true, isGameStartedState.getValue());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
/*
    @Test
    void testGameStartMessage(){
        this.gameClient.startGame();
        Mockito.verify(client,Mockito.times(1)).sendTCP(new Requests.StartGameMessage());
    }

    @Test
    void testSetCorrectCard(){
        Card card = new Card(Symbol.HERZ, CardValue.ASS);
        this.gameClient.setCard(card);
        Requests.PlayerSetCard req = new Requests.PlayerSetCard();
        req.setCard(card);
        Mockito.verify(client, Mockito.times(1)).sendTCP(req);
    }

    @Test
    void testSetSchlag(){
        MutableLiveData<Boolean> schlag = Mockito.mock(MutableLiveData.class);
        this.gameClient.setSetSchlag(schlag);
        this.gameClient.setSchlag(CardValue.ACHT);
        Requests.PlayerSetSchlag req = new Requests.PlayerSetSchlag(CardValue.ACHT);
        Mockito.verify(client,Mockito.times(1)).sendTCP(req);
    }

    @Test
    void testSetTrump(){
        MutableLiveData<Boolean> setTrump = Mockito.mock(MutableLiveData.class);
        this.gameClient.setSetTrump(setTrump);
        this.gameClient.setTrump(Symbol.HERZ);
        Requests.PlayerSetTrump req = new Requests.PlayerSetTrump(Symbol.HERZ);

        Mockito.verify(client,Mockito.times(1)).sendTCP(req);
    }


*/
}

