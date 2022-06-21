package com.example.piatinkpartyapp.networking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseCheatingPenalty;
import com.example.piatinkpartyapp.networking.responses.responseConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.responses.responseEndOfGame;
import com.example.piatinkpartyapp.networking.responses.responseEndOfRound;
import com.example.piatinkpartyapp.networking.responses.responseGameStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseHosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseIsCheater;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.responses.responsePensionistLnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responsePlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.responses.responseReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseSchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseSendHandCards;
import com.example.piatinkpartyapp.networking.responses.responseSendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseServerMessage;
import com.example.piatinkpartyapp.networking.responses.responseUpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.responses.responseUpdateScoreboard;
import com.example.piatinkpartyapp.networking.responses.responseVoteForNextGame;
import com.example.piatinkpartyapp.networking.responses.responseWattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseMixedCards;
import com.example.piatinkpartyapp.networking.responses.responsePlayerDisconnected;
import com.example.piatinkpartyapp.networking.requests.requestExposePossibleCheater;
import com.example.piatinkpartyapp.networking.requests.requestForceVoting;
import com.example.piatinkpartyapp.networking.requests.requestMixCardsRequest;
import com.example.piatinkpartyapp.networking.requests.requestPlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetCard;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetSchlag;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetTrump;
import com.example.piatinkpartyapp.networking.requests.requestSendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestSendToAllChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestStartGameMessage;
import com.example.piatinkpartyapp.networking.requests.requestVoteForNextGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    private static GameClient INSTANCE = null;
    private int playerID;
    private Client client;
    private ExecutorService executorService;
    int x = 11;

    //for testing purposes
    public GameClient(Client client){
        this.client = client;
        this.executorService = Executors.newFixedThreadPool(1);
    }

    public GameClient(String gameServer_IP) {
        initLiveData();
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            NetworkHandler.GAMESERVER_IP = gameServer_IP;
            // we to start this in a new thread, so we don't block the main Thread!!
            client = new Client();
            // this line of code has to run before we start / bind / connect to the server !
            NetworkHandler.register(client.getKryo());
            client.start();
            try {
                client.connect(10000, NetworkHandler.GAMESERVER_IP, NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
            } catch (IOException ignored){}

            this.playerID = client.getID();
            startListener();

        });

    }

    public static GameClient getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new GameClient(NetworkHandler.GAMESERVER_IP);
        }
        return INSTANCE;
    }

    public String getPlayerID() {
        return "Player " + playerID;
    }

    public int getID(){
        return playerID;
    }

    private void startListener() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }


            @Override
            public void disconnected(Connection connection) {
                try {

                } catch (Exception ignored) {
                }
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (object instanceof responseConnectedSuccessfully) {
                        handleConnectedSuccessfully((responseConnectedSuccessfully) object);
                    } else if (object instanceof responseReceiveEndToEndChatMessage) {
                        handleReceiveEndToEndMessage((responseReceiveEndToEndChatMessage) object);
                    } else if (object instanceof responseReceiveToAllChatMessage) {
                        handleReceiveToAllChatMessage((responseReceiveToAllChatMessage) object);
                    } else if (object instanceof responseGameStartedClientMessage) {
                        handleGameStartedClientMessage((responseGameStartedClientMessage) object);
                    } else if (object instanceof responseSendHandCards) {
                        handleSendHandCards((responseSendHandCards) object);
                    } else if (object instanceof responseNotifyPlayerYourTurn) {
                        handleNotifyPlayerYourTurn((responseNotifyPlayerYourTurn) object);
                    } else if (object instanceof responsePlayerGetHandoutCard) {
                        handlePlayerGetHandoutCard((responsePlayerGetHandoutCard) object);
                    } else if (object instanceof responseEndOfRound) {
                        handleEndOfRound((responseEndOfRound) object);
                    } else if (object instanceof responseVoteForNextGame){
                        handleVoteForNextGame();
                    } else if (object instanceof responseSendPlayedCardToAllPlayers) {
                        handleSendPlayedCardToAllPlayers((responseSendPlayedCardToAllPlayers) object);
                    } else if (object instanceof responseSendTrumpToAllPlayers) {
                        handleSendTrumpToAllPlayers((responseSendTrumpToAllPlayers) object);
                    } else if (object instanceof responseNotifyPlayerToSetSchlag) {
                        handleNotifyPlayerToSetSchlag((responseNotifyPlayerToSetSchlag) object);
                    } else if (object instanceof responseNotifyPlayerToSetTrump) {
                        handleNotifyPlayerToSetTrump((responseNotifyPlayerToSetTrump) object);
                    } else if(object instanceof responseUpdatePointsWinnerPlayer){
                        handleUpdatePointsWinnerPlayer((responseUpdatePointsWinnerPlayer) object);
                    } else if (object instanceof responseSchnopsnStartedClientMessage) {
                        handleSchnopsnStartedClientMessage((responseSchnopsnStartedClientMessage) object);
                    } else if (object instanceof responseWattnStartedClientMessage){
                        handleWattnStartedClientMessage((responseWattnStartedClientMessage) object);
                    } else if (object instanceof responsePensionistLnStartedClientMessage){
                        handlePensionistLnStartedClientMessage((responsePensionistLnStartedClientMessage) object);
                    } else if (object instanceof responseHosnObeStartedClientMessage){
                        handleHosnObeStartedClientMessage((responseHosnObeStartedClientMessage) object);
                    }else if(object instanceof responsePlayerDisconnected){
                        handlePlayerDisconnected((responsePlayerDisconnected)object);
                    }else if(object instanceof responseMixedCards){
                        handleMixedCards((responseMixedCards)object);
                    }else if(object instanceof responseIsCheater){
                        handleIsCheater((responseIsCheater)object);
                    } else if (object instanceof responseSendRoundWinnerPlayerToAllPlayers) {
                        handleSendRoundWinnerPlayerToAllPlayers((responseSendRoundWinnerPlayerToAllPlayers) object);
                    } else if (object instanceof responseUpdateScoreboard) {
                        handleUpdateScoreboard((responseUpdateScoreboard) object);
                    } else if(object instanceof responseCheatingPenalty){
                        handleCheatingPenalty((responseCheatingPenalty) object);
                    } else if(object instanceof responseSendSchlagToAllPlayers){
                        handleSendSchlagToAllPlayers((responseSendSchlagToAllPlayers) object);
                    } else if(object instanceof responseServerMessage){
                        handleServerMessage((responseServerMessage) object);
                    } else if(object instanceof responseEndOfGame){
                        handleEndOfGame((responseEndOfGame) object);
                    } else if(object instanceof responsePlayerDisconnected){
                        handlePlayerDisconnected((responsePlayerDisconnected) object);
                    }
                } catch (Exception e) {
                    LOG.info(e.toString());
                }
            }
        });
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handleIsCheater(responseIsCheater object) {
        boolean isCheater = object.isCheater;

        cheaterExposed.postValue(isCheater);
    }

    private void handleCheatingPenalty(responseCheatingPenalty object) {
        cheatingExposed.postValue(true);
    }

    private void handleVoteForNextGame() {
        voteForNextGame.postValue(true);
        LOG.info("VoteForNextGame received from the server");
    }

    private void handleEndOfGame(responseEndOfGame object) {
        gameStarted.postValue(false);
    }

    private void handleEndOfRound(responseEndOfRound object) {
        responseEndOfRound response = object;

        endOfRound.postValue(response.playerID);

        LOG.info("End of round!");
    }

    private void handlePlayerGetHandoutCard(responsePlayerGetHandoutCard object) {
        responsePlayerGetHandoutCard response = object;

        handoutCard.postValue(response.card);

        LOG.info("Handout card received for player: " + response.playerID);
    }

    private void handleNotifyPlayerYourTurn(responseNotifyPlayerYourTurn object) {
        responseNotifyPlayerYourTurn response = object;

        myTurn.postValue(true);

        LOG.info("It's your turn! player: " + response.playerID);
    }

    private void handleSendHandCards(responseSendHandCards object) {
        responseSendHandCards response = object;

        handCards.postValue(response.cards);

        LOG.info("Handcards received for player: " + response.playerID);
    }

    private void handleGameStartedClientMessage(responseGameStartedClientMessage object) {
        responseGameStartedClientMessage response =
                object;

        gameStarted.postValue(true);

        LOG.info("Game started by server");
    }

    private void handleSchnopsnStartedClientMessage(responseSchnopsnStartedClientMessage object) {
        responseSchnopsnStartedClientMessage response =
                object;

        schnopsnStarted.postValue(true);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Schnopsn Game started by server after voting");
    }

    private void handleWattnStartedClientMessage(responseWattnStartedClientMessage object) {
        responseWattnStartedClientMessage response =
                object;

        // notify UI: game has started
        wattnStarted.postValue(true);
        schnopsnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Wattn Game started by server after voting");
    }

    private void handlePensionistLnStartedClientMessage(responsePensionistLnStartedClientMessage object) {
        responsePensionistLnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(true);
        hosnObeStarted.postValue(false);

        LOG.info("Pensionistln Game started by server after voting");
    }

    private void handleHosnObeStartedClientMessage(responseHosnObeStartedClientMessage object) {
        responseHosnObeStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(true);

        LOG.info("Hosn Obe Game started by server after voting");
    }

    private void handleConnectedSuccessfully(responseConnectedSuccessfully object) {
        responseConnectedSuccessfully response =
                object;

        // notify UI: connection information
        connectionState.postValue(response.isConnected);

        if (response.isConnected && playerID == response.playerID) {
            LOG.info("Client connected successfully to server : " + NetworkHandler.GAMESERVER_IP +
                    ", Client ID within game: " + response.playerID);
        } else {
            LOG.info("Client cannot connect to server : " + NetworkHandler.GAMESERVER_IP);
        }
    }

    private void handleServerMessage(responseServerMessage object) {
        responseServerMessage response = object;

        serverMessage.postValue(response.message);

        LOG.info("Message from server received: " + response.message);
    }

    private void handleSendPlayedCardToAllPlayers(responseSendPlayedCardToAllPlayers object) {
        responseSendPlayedCardToAllPlayers response = object;

        Card cardPlayed = response.card;

        LOG.info("Played card " + cardPlayed.getSymbol().toString() + cardPlayed.getCardValue().toString() + " from player: " + response.playerID + " was received");

        //notify UI
        playedCard.postValue(object);
    }

    private void handleSendTrumpToAllPlayers(responseSendTrumpToAllPlayers object) {
        responseSendTrumpToAllPlayers response = object;

        Symbol currentTrump = response.trump;

        LOG.info("Trump: " + trump.toString() + " was sent to player!");

        //notify UI
        trump.postValue(currentTrump);
    }
    private void handleSendSchlagToAllPlayers(responseSendSchlagToAllPlayers object){
        responseSendSchlagToAllPlayers response = object;
        CardValue currentSchlag = response.schlag;
        LOG.info("Schlag: "+ schlag.toString() + "was sent to player!");
        schlag.postValue(currentSchlag);
    }

    private void handleNotifyPlayerToSetSchlag(responseNotifyPlayerToSetSchlag object) {
        responseNotifyPlayerToSetSchlag response =
                object;

        // notify UI: to set schlag
        setSchlag.postValue(true);

        LOG.info("Please set schlag!");
    }

    private void handleNotifyPlayerToSetTrump(responseNotifyPlayerToSetTrump object) {
        responseNotifyPlayerToSetTrump response =
                object;

        // notify UI: to set trump
        setTrump.postValue(true);

        LOG.info("Please set trump!");
    }

    private void handleUpdatePointsWinnerPlayer(responseUpdatePointsWinnerPlayer object){
        responseUpdatePointsWinnerPlayer response = object;

        points.postValue(response.totalPoints);
    }

    private void handlePlayerDisconnected(responsePlayerDisconnected object){
        LOG.info("PLayer has disconected ID:" + object.playerID);

        disconnectedPlayer.postValue(object.playerID);
    }

    //mixed cards
    private void handleMixedCards(responseMixedCards object){
        responseMixedCards response = object;
        mixedCards.postValue(true);
        LOG.info("mixed cards");
    }

    private void handleSendRoundWinnerPlayerToAllPlayers(responseSendRoundWinnerPlayerToAllPlayers object) {
        responseSendRoundWinnerPlayerToAllPlayers response = object;

        winnerId.postValue(response.winnerPlayerID);

        LOG.info("Player: " + response.winnerPlayerID + " has won the round!");
    }

    private void handleUpdateScoreboard(responseUpdateScoreboard object) {
        responseUpdateScoreboard response = object;

        players.postValue(response.players);

        LOG.info("Update Scoreboard!");
    }
    /////////////////// END - Handler Methods !!! ///////////////////


    // Generic function which should be used for sending packets to server!
    public void sendPacket(IPackets packet) {
        executorService.execute(() -> {
            client.sendTCP(packet);
        });
    }
    // Call this method from client to start a game

    public void startGame() {
        sendPacket(new requestStartGameMessage());
    }

    public void setCard(Card card) {

        requestPlayerSetCard request = new requestPlayerSetCard();
        request.card =  card;
        sendPacket(request);

        myTurn.postValue(false);
    }

   public void mixCards(){
        requestMixCardsRequest request = new requestMixCardsRequest();
        sendPacket(request);
    }

    public void setSchlag(CardValue schlag) {
        requestPlayerSetSchlag request = new requestPlayerSetSchlag(schlag);
        sendPacket(request);
        setSchlag.postValue(false);
    }

    public void setTrump(Symbol trump) {
        requestPlayerSetTrump request = new requestPlayerSetTrump(trump);
        sendPacket(request);
        setTrump.postValue(false);
    }

    /////////////////// START - CHAT - LOGiC ///////////////////

    // Will be used for updating UI when Client receives Messages from Server
    private MutableLiveData<ArrayList<ChatMessage>> chatMessages;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    private void handleReceiveToAllChatMessage(responseReceiveToAllChatMessage object) {
        responseReceiveToAllChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);

        ArrayList<ChatMessage> value = chatMessages.getValue();
        value.add(msg);
        chatMessages.postValue(value);
    }

    private void handleReceiveEndToEndMessage(responseReceiveEndToEndChatMessage object) {
        responseReceiveEndToEndChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
    }

    public void sendEndToEndMessage(String message, int to) {
        requestSendEndToEndChatMessage request = new requestSendEndToEndChatMessage(message, playerID, to);
        sendPacket(request);
    }

    public void sendToAll(String message) {
        requestSendToAllChatMessage request = new requestSendToAllChatMessage(message, playerID);
        sendPacket(request);
    }

    public void initLiveData() {
        chatMessages = new MutableLiveData<>();
        ArrayList<ChatMessage> dummy = new ArrayList<>();

        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 4", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 3", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 3", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 3", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 3", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 3", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm", ChatMessage.MessageType.OUT));

        chatMessages.setValue(dummy);

        //for main game uis
        initLiveDataMainGameUIs();

    }
    /////////////////// END - CHAT - LOGiC ///////////////////


    ////////////// START - MainGameUIs - LOGiC //////////////

    private MutableLiveData<ArrayList<Card>> handCards;
    private MutableLiveData<Boolean> connectionState;
    private MutableLiveData<Boolean> myTurn;
    private MutableLiveData<Boolean> gameStarted;
    private MutableLiveData<Card> handoutCard;
    private MutableLiveData<Integer> endOfRound;
    private MutableLiveData<Boolean> voteForNextGame;
    private MutableLiveData<responseSendPlayedCardToAllPlayers> playedCard;
    private MutableLiveData<Symbol> trump;
    private MutableLiveData<CardValue> schlag;
    private MutableLiveData<Integer> points;
    private MutableLiveData<Boolean> setSchlag;
    private MutableLiveData<Boolean> setTrump;
    private MutableLiveData<Boolean> schnopsnStarted;
    private MutableLiveData<Boolean> wattnStarted;
    private MutableLiveData<Boolean> pensionistlnStarted;
    private MutableLiveData<Boolean> hosnObeStarted;
    private MutableLiveData<Boolean> mixedCards;
    private MutableLiveData<Integer> winnerId;
    private MutableLiveData<Boolean> cheaterExposed;
    private MutableLiveData<Boolean> cheatingExposed;
    private MutableLiveData<Map<String, Integer>> players;
    private MutableLiveData<String> serverMessage;
    private MutableLiveData<Integer> disconnectedPlayer;

    public LiveData<Boolean> getConnectionState(){
        return connectionState;
    }

    public LiveData<ArrayList<Card>> getHandCards(){
        return handCards;
    }

    public LiveData<Boolean> isMyTurn(){
        return myTurn;
    }

    public LiveData<Boolean> mixedCards(){
        return mixedCards;
    }
    public LiveData<Boolean> isGameStarted(){
        return gameStarted;
    }

    public LiveData<Integer> isEndOfRound() {
        return endOfRound;
    }

    public LiveData<Card> getHandoutCard(){
        return handoutCard;
    }

    public LiveData<Boolean> isVotingForNextGame() { return voteForNextGame; }

    public LiveData<responseSendPlayedCardToAllPlayers> getPlayedCard(){
        return playedCard;
    }

    public LiveData<Symbol> getTrump(){
        return trump;
    }

    public LiveData<CardValue> getSchlag(){return schlag;}

    public LiveData<Integer> getPoints() {
        return points;
    }

    public LiveData<Boolean> isSetSchlag() {
        return  setSchlag;
    }

    public LiveData<Boolean> isSetTrump() {
        return setTrump;
    }

    public LiveData<Boolean> isSchnopsnStarted() {
        return schnopsnStarted;
    }

    public LiveData<Boolean> isWattnStarted() {
        return wattnStarted;
    }

    public LiveData<Boolean> isPensionistlnStarted() {
        return pensionistlnStarted;
    }

    public LiveData<Boolean> isHosnObeStarted() {
        return hosnObeStarted;
    }

    public LiveData<Integer> getWinnerId() { return winnerId; }

    public LiveData<Boolean> isCheaterExposed() { return cheaterExposed; }

    public LiveData<Map<String, Integer>> getPlayers() {
        return players;
    }

    private void initLiveDataMainGameUIs(){
        handCards = new MutableLiveData<>();
        connectionState = new MutableLiveData<>();
        myTurn = new MutableLiveData<>();
        mixedCards = new MutableLiveData<Boolean>();
        gameStarted = new MutableLiveData<>();
        handoutCard = new MutableLiveData<>();
        endOfRound = new MutableLiveData<Integer>();
        voteForNextGame = new MutableLiveData<>();
        playedCard = new MutableLiveData<>();
        trump = new MutableLiveData<>();
        schlag = new MutableLiveData<>();
        points = new MutableLiveData<>();
        setTrump = new MutableLiveData<>();
        setSchlag = new MutableLiveData<>();
        schnopsnStarted = new MutableLiveData<>();
        wattnStarted = new MutableLiveData<>();
        pensionistlnStarted = new MutableLiveData<>();
        hosnObeStarted = new MutableLiveData<>();
        winnerId = new MutableLiveData<>();
        cheaterExposed = new MutableLiveData<>();
        cheatingExposed = new MutableLiveData<>();
        players = new MutableLiveData<>();
        serverMessage = new MutableLiveData<>();
        disconnectedPlayer = new MutableLiveData<>();
    }


    public void setSetTrump(MutableLiveData<Boolean> trump) {
        this.setTrump = trump;
    }

    public void setSetSchlag(MutableLiveData<Boolean> setSchlag) {
        this.setSchlag = setSchlag;
    }

    public void sendVoteForNextGame(GameName nextGame){
        requestVoteForNextGame request =
                new requestVoteForNextGame(nextGame);

        sendPacket(request);

        voteForNextGame.postValue(false);

        LOG.info("Client voted for " + nextGame.toString());
    }

    public void forceVoting(){
        requestForceVoting request = new requestForceVoting();

        sendPacket(request);

        LOG.info("Voting force message has been sent to the server");
    }

    public void cheatRequest() {
        requestPlayerRequestsCheat request = new requestPlayerRequestsCheat();

        sendPacket(request);

        LOG.info("CheatRequest was sent");
    }

    public void exposePossibleCheater(Integer playerId) {
        requestExposePossibleCheater request = new requestExposePossibleCheater(playerId);
        sendPacket(request);
        LOG.info("ExposePossibleCheater Request was sent for playerID : " + playerId);
    }

    public void notifyVote() {
        voteForNextGame.postValue(true);
    }

    public LiveData<Boolean> isCheatingExposed() {
        return cheatingExposed;
    }

    public void sendVoteForGameEnd() {
        requestVoteForNextGame request = new requestVoteForNextGame();

        //sends a Requests.VoteForNextGame packet without setting a nextGame
        sendPacket(request);

        LOG.info("Client voted for the game to end");
    }

    public LiveData<String> getServerMessage() {
        return serverMessage;
    }

    public LiveData<Integer> getDisconnectedPlayer(){
        return disconnectedPlayer;
    }

    public void disconnectFromGame() {
        client.stop();
        INSTANCE = null;
    }

    /////////////// END - MainGameUIs - LOGiC ///////////////
}