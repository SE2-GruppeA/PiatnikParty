package com.example.piatinkpartyapp.clientuilogic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.responses.responseSendPlayedCardToAllPlayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ClientViewModel extends ViewModel {
    private static final String TAG = "ClientViewModel";


    private GameClient client = GameClient.getInstance();

    private GameServer server = null;

    public void startNewGameServer(){
        server = GameServer.getInstance();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientViewModel() throws IOException {
         /*
    // Don't know why this methode is empty
     */
    }

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

    //when game ends, the scoreboard is shown
    //this value is used to close the SchnopsnFragment after the scoreboard is shown
    private MutableLiveData<Boolean> closeGameAfterScoreboard = new MutableLiveData<>();
    //when true: Scoreboard Fragment sets closeGameAfterScoreboard to true when closed
    private Boolean closeGameScoreboard = false;

    public LiveData<Boolean> getConnectionState(){
        return client.getConnectionState();
    }

    public LiveData<ArrayList<Card>> getHandCards(){
        return client.getHandCards();
    }

    public LiveData<Boolean> isMyTurn(){
        return client.isMyTurn();
    }

    public LiveData<Boolean>mixedCards(){return client.mixedCards();}

    public LiveData<Boolean> isGameStarted(){
        return client.isGameStarted();
    }

    public LiveData<Boolean> isEndOfGame(){
        return client.isEndOfGame();
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

    public LiveData<ArrayList<Integer>> isEndOfRound(){
        return client.isEndOfRound();
    }

    public LiveData<responseSendPlayedCardToAllPlayers> getPlayedCard() {
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
        client.setTrump(trump);
    }

    public void cheat() {
        cheatRequest();
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
    public void mixCards(){client.mixCards();}

    public void cheatRequest() {
        client.cheatRequest();
    }

    public void exposePossibleCheater(Integer playerId) {
        client.exposePossibleCheater(playerId);
    }

    public void notifyVote() {
        client.notifyVote();
    }

    public LiveData<Integer> getWinnerId() {
        return client.getWinnerId();
    }

    public LiveData<Boolean> isCheaterExposed() {
        return client.isCheaterExposed();
    }

    public LiveData<Boolean> isCheatingExposed() {
        return client.isCheatingExposed();
    }

    public LiveData<Map<String, Integer>> getPlayers() {
        return client.getPlayers();
    }

    public LiveData<String> getServerMessage() {
        return client.getServerMessage();
    }

    public LiveData<Integer> getPlayerDisconnected() {
        return client.getDisconnectedPlayer();
    }

    public void disconnectFromGame(){
        client.disconnectFromGame();
    }

    public void leaveGame() {
        if(!isEndOfGame().getValue()){
            if(client != null){
                if(server!=null){
                    server.closeGame();
                }
            }
        }

        disconnectFromGame();
    }

    public void getInstance() throws IOException {
        client = client.getInstance();
    }

    public LiveData<Boolean> getCloseGameAfterScoreboard() {
        return closeGameAfterScoreboard;
    }

    public void closeGame(){
        closeGameAfterScoreboard.postValue(true);
    }

    public void setCloseGameScoreboard(Boolean close){
        closeGameScoreboard = close;
    }

    public Boolean getCloseGameScoreboard(){
        return closeGameScoreboard;
    }

    public LiveData<String> getWrongNumberOfPlayers() {
        return client.getWrongNumberOfPlayers();
    }

    /////////////// END - MainGameUIs - LOGiC ///////////////
}
