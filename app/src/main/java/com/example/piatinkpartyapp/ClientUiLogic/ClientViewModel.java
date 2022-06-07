package com.example.piatinkpartyapp.ClientUiLogic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.Responses;

import java.io.IOException;
import java.util.ArrayList;

public class ClientViewModel extends ViewModel {
    private static final String TAG = "ClientViewModel";


    private GameClient client = GameClient.getInstance();

    public ClientViewModel() throws IOException { }

    public String getPlayerID() {
        return String.valueOf(client.getPlayerID());
    }

    ////////////// START - Chat UI - LOGiC //////////////
    // by default false !
    public Boolean firstTimeOpenedChatFragment = false;


    public int counter = 0;
    public int expectedCounterForCheatWindow;
    public String cheatCode;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() { return client.getChatMessages(); }

    public void sendToAllChatMessage(String message) {
        client.sendToAll(message);
    }
    ////////////// END - Chat UI - LOGiC //////////////


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

    public LiveData<Boolean> isVotingForNextGame(){
        return client.isVotingForNextGame();
    }

    public void forceVoting(){
        client.forceVoting();
    }

    public void voteForNextGame(GameName nextGame){
        client.sendVoteForNextGame(nextGame);
    }

    public LiveData<Boolean> isEndOfRound(){
        return client.isEndOfRound();
    }

    public LiveData<Responses.SendPlayedCardToAllPlayers> getPlayedCard() {
        return client.getPlayedCard();
    }

    public LiveData<Symbol> getTrump() {
        return client.getTrump();
    }

    public LiveData<Integer> getPoints() {
        return client.getPoints();
    }

    public LiveData<Boolean> schlagToSet(){return client.isSetSchlag();}
    public LiveData<Boolean> trumpToSet(){return client.isSetTrump();}

    public void setTrump(Symbol trump) {
        client.setSetTrump(trump);
    }

    public void cheat() {
        //todo: implement client code
        Log.d(TAG, "YOU ARE CHEATING NOW");

    }
    public void setSchlag(CardValue schlag){client.setSchlag(schlag);}
    public LiveData<CardValue> getSchlag(){return client.getSchlag();}
    public LiveData<Boolean> isSetSchlag() {
        return client.isSetSchlag();
    }
    public LiveData<Boolean> isSetTrump() {
        return client.isSetTrump();
    }
    public LiveData<Boolean> isSchnopsnStarted() {
        return client.isSchnopsnStarted();
    }
    public LiveData<Boolean> isWattnStarted() {
        return client.isWattnStarted();
    }
    public LiveData<Boolean> isPensionistlnStarted() {
        return client.isPensionistlnStarted();
    }
    public LiveData<Boolean> isHosnObeStarted() {
        return client.isHosnObeStarted();
    }

    public void cheatRequest() {
        client.cheatRequest();
    }



    /////////////// END - MainGameUIs - LOGiC ///////////////
}
