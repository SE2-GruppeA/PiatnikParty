package com.example.piatinkpartyapp.ClientUiLogic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.networking.GameClient;

import java.io.IOException;
import java.util.ArrayList;

public class ClientViewModel extends ViewModel {
    private GameClient client = GameClient.getInstance();

    public ClientViewModel() throws IOException { }

    public String getPlayerID() {
        return String.valueOf(client.getPlayerID());
    }

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() { return client.getChatMessages(); }

    public void sendToAllChatMessage(String message) {
        client.sendToAll(message);
    }

    ////////////// START - MainGameUIs - LOGiC //////////////

    public LiveData<Boolean> getConnectionState(){
        return client.getConnectionState();
    }

    public LiveData<ArrayList<Card>> getHandCards(){
        return client.getHandCards();
    }

    public LiveData<Boolean> isMyTurn(){
        return client.isMyTurn();
    }

    public LiveData<Boolean> isGameStarted(){
        return client.isGameStarted();
    }

    public LiveData<Card> getHandoutCard(){
        return client.getHandoutCard();
    }

    public void startGame() {
        client.startGame();
    }


    public void setCard(Card c) {
        client.setCard(c);
    }

    public LiveData<Boolean> selectTrump(){return client.selectTrump();}
   /* public LiveData<CardValue> getHit(){return client.getHit();}*/
public void setTrump(Symbol trump){
    client.setTrump(trump);
}
    /////////////// END - MainGameUIs - LOGiC ///////////////
}
