
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
}

