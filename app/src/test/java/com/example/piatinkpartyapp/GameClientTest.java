
package com.example.piatinkpartyapp;

import androidx.lifecycle.MutableLiveData;

import com.esotericsoftware.kryonet.Client;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.Requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class GameClientTest {

    private GameClient gameClient;

    @Mock
    private Client client;

    @BeforeEach
    public void setup(){
        this.client = Mockito.mock(Client.class);

        this.gameClient = new GameClient(client);
    }

    @Test
    public void testGameStartMessage(){
        this.gameClient.startGame();
        Mockito.verify(client,Mockito.times(1)).sendTCP(new Requests.StartGameMessage());
    }

    /*@Test
    public void testSetCorrectCard(){
        Card card = new Card(Symbol.HERZ, CardValue.ASS);
        this.gameClient.setCard(card);
        Requests.PlayerSetCard req = new Requests.PlayerSetCard();
        req.setCard(card);
        Mockito.verify(client, Mockito.times(1)).sendTCP(req);
    }

    @Test
    public void testSetSchlag(){
        MutableLiveData<Boolean> schlag = Mockito.mock(MutableLiveData.class);
        this.gameClient.setSetSchlag(schlag);
        this.gameClient.setSchlag(CardValue.ACHT);
        Requests.PlayerSetSchlag req = new Requests.PlayerSetSchlag(CardValue.ACHT);
        Mockito.verify(client,Mockito.times(1)).sendTCP(req);
    }

    @Test
    public void testSetTrump(){
        MutableLiveData<Boolean> setTrump = Mockito.mock(MutableLiveData.class);
        this.gameClient.setSetTrump(setTrump);
        this.gameClient.setTrump(Symbol.HERZ);
        Requests.PlayerSetTrump req = new Requests.PlayerSetTrump(Symbol.HERZ);

        Mockito.verify(client,Mockito.times(1)).sendTCP(req);
    }


*/
}

