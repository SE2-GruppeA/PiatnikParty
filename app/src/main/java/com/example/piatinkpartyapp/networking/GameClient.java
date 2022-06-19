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
import com.example.piatinkpartyapp.networking.Responses.Response_CheatingPenalty;
import com.example.piatinkpartyapp.networking.Responses.Response_ConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.Responses.Response_EndOfRound;
import com.example.piatinkpartyapp.networking.Responses.Response_GameStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_HosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_IsCheater;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.Responses.Response_PensionistlnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_PlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.Responses.Response_ReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_ReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_SchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_SendHandCards;
import com.example.piatinkpartyapp.networking.Responses.Response_SendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdateScoreboard;
import com.example.piatinkpartyapp.networking.Responses.Response_VoteForNextGame;
import com.example.piatinkpartyapp.networking.Responses.Response_WattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_mixedCards;
import com.example.piatinkpartyapp.networking.Responses.Response_playerDisconnected;
import com.example.piatinkpartyapp.networking.Requests.Request_ExposePossibleCheater;
import com.example.piatinkpartyapp.networking.Requests.Request_ForceVoting;
import com.example.piatinkpartyapp.networking.Requests.Request_MixCardsRequest;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetCard;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetSchlag;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetTrump;
import com.example.piatinkpartyapp.networking.Requests.Request_SendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_SendToAllChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_StartGameMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_VoteForNextGame;

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
            } catch (IOException e) {
                //e.printStackTrace();
            }

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

    private void startListener() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }


            @Override
            public void disconnected(Connection connection) {
                try {

                } catch (Exception e) {
                    //e.printStackTrace();
                }
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (object instanceof Response_ConnectedSuccessfully) {
                        handle_ConnectedSuccessfully((Response_ConnectedSuccessfully) object);
                    } else if (object instanceof Response_ReceiveEndToEndChatMessage) {
                        handle_ReceiveEndToEndMessage((Response_ReceiveEndToEndChatMessage) object);
                    } else if (object instanceof Response_ReceiveToAllChatMessage) {
                        handle_ReceiveToAllChatMessage((Response_ReceiveToAllChatMessage) object);
                    } else if (object instanceof Response_GameStartedClientMessage) {
                        handle_GameStartedClientMessage((Response_GameStartedClientMessage) object);
                    } else if (object instanceof Response_SendHandCards) {
                        handle_SendHandCards((Response_SendHandCards) object);
                    } else if (object instanceof Response_NotifyPlayerYourTurn) {
                        handle_NotifyPlayerYourTurn((Response_NotifyPlayerYourTurn) object);
                    } else if (object instanceof Response_PlayerGetHandoutCard) {
                        handle_PlayerGetHandoutCard((Response_PlayerGetHandoutCard) object);
                    } else if (object instanceof Response_EndOfRound) {
                        handle_EndOfRound((Response_EndOfRound) object);
                    } else if (object instanceof Response_VoteForNextGame){
                        handle_VoteForNextGame();
                    } else if (object instanceof Response_SendPlayedCardToAllPlayers) {
                        handle_SendPlayedCardToAllPlayers((Response_SendPlayedCardToAllPlayers) object);
                    } else if (object instanceof Response_SendTrumpToAllPlayers) {
                        handle_SendTrumpToAllPlayers((Response_SendTrumpToAllPlayers) object);
                    } else if (object instanceof Response_NotifyPlayerToSetSchlag) {
                        handle_NotifyPlayerToSetSchlag((Response_NotifyPlayerToSetSchlag) object);
                    } else if (object instanceof Response_NotifyPlayerToSetTrump) {
                        handle_NotifyPlayerToSetTrump((Response_NotifyPlayerToSetTrump) object);
                    } else if(object instanceof Response_UpdatePointsWinnerPlayer){
                        handle_UpdatePointsWinnerPlayer((Response_UpdatePointsWinnerPlayer) object);
                    } else if (object instanceof Response_SchnopsnStartedClientMessage) {
                        handle_SchnopsnStartedClientMessage((Response_SchnopsnStartedClientMessage) object);
                    } else if (object instanceof Response_WattnStartedClientMessage){
                        handle_WattnStartedClientMessage((Response_WattnStartedClientMessage) object);
                    } else if (object instanceof Response_PensionistlnStartedClientMessage){
                        handle_PensionistlnStartedClientMessage((Response_PensionistlnStartedClientMessage) object);
                    } else if (object instanceof Response_HosnObeStartedClientMessage){
                        handle_HosnObeStartedClientMessage((Response_HosnObeStartedClientMessage) object);
                    }else if(object instanceof Response_playerDisconnected){
                        handle_PlayerDisconnected((Response_playerDisconnected)object);
                    }else if(object instanceof Response_mixedCards){
                        handle_MixedCards((Response_mixedCards)object);
                    }else if(object instanceof Response_IsCheater){
                        handle_isCheater((Response_IsCheater)object);
                    } else if (object instanceof Response_SendRoundWinnerPlayerToAllPlayers) {
                        handle_SendRoundWinnerPlayerToAllPlayers((Response_SendRoundWinnerPlayerToAllPlayers) object);
                    } else if (object instanceof Response_UpdateScoreboard) {
                        handle_UpdateScoreboard((Response_UpdateScoreboard) object);
                    } else if(object instanceof Response_CheatingPenalty){
                        handle_CheatingPenalty((Response_CheatingPenalty) object);
                    }else if(object instanceof Response_SendSchlagToAllPlayers){
                        handle_SendSchlagToAllPlayers((Response_SendSchlagToAllPlayers) object);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.info(e.toString());
                }
            }
        });
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_isCheater(Response_IsCheater object) {
        // todo: handle live data and update ui
        boolean isCheater = object.isCheater;

        cheaterExposed.postValue(isCheater);
        /**
         * If isCheater == true, einfach normal anzeigen das man einen cheater exposed hat
         * If false, anzeigen das man -10 punkte verloren hat.
         */
    }

    private void handle_CheatingPenalty(Response_CheatingPenalty object) {
        cheatingExposed.postValue(true);
    }

    private void handle_VoteForNextGame() {
        voteForNextGame.postValue(true);
        LOG.info("VoteForNextGame received from the server");
    }

    private void handle_EndOfRound(Response_EndOfRound object) {
        Response_EndOfRound response =
                object;

        // notify UI: round of player is over
        endOfRound.postValue(response.playerID);

        LOG.info("End of round!");
    }

    private void handle_PlayerGetHandoutCard(Response_PlayerGetHandoutCard object) {
        Response_PlayerGetHandoutCard response =
                object;

        // notify UI: clients gets one card
        handoutCard.postValue(response.card);

        LOG.info("Handout card received for player: " + response.playerID);
    }

    private void handle_NotifyPlayerYourTurn(Response_NotifyPlayerYourTurn object) {
        Response_NotifyPlayerYourTurn response =
                object;

        // notify UI: its the clients turn
        myTurn.postValue(true);

        LOG.info("It's your turn! player: " + response.playerID);
    }

    private void handle_SendHandCards(Response_SendHandCards object) {
        Response_SendHandCards response =
                object;

        // notify UI: send handcards to SchnopsnFragment
        handCards.postValue(response.cards);

        LOG.info("Handcards received for player: " + response.playerID);
    }

    private void handle_GameStartedClientMessage(Response_GameStartedClientMessage object) {
        Response_GameStartedClientMessage response =
                object;

        // notify UI: game has started
        gameStarted.postValue(true);

        LOG.info("Game started by server");
    }

    private void handle_SchnopsnStartedClientMessage(Response_SchnopsnStartedClientMessage object) {
        Response_SchnopsnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(true);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Schnopsn Game started by server after voting");
    }

    private void handle_WattnStartedClientMessage(Response_WattnStartedClientMessage object) {
        Response_WattnStartedClientMessage response =
                object;

        // notify UI: game has started
        wattnStarted.postValue(true);
        schnopsnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Wattn Game started by server after voting");
    }

    private void handle_PensionistlnStartedClientMessage(Response_PensionistlnStartedClientMessage object) {
        Response_PensionistlnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(true);
        hosnObeStarted.postValue(false);

        LOG.info("Pensionistln Game started by server after voting");
    }

    private void handle_HosnObeStartedClientMessage(Response_HosnObeStartedClientMessage object) {
        Response_HosnObeStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(true);

        LOG.info("Hosn Obe Game started by server after voting");
    }

    private void handle_ConnectedSuccessfully(Response_ConnectedSuccessfully object) {
        Response_ConnectedSuccessfully response =
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

    private void handle_SendPlayedCardToAllPlayers(Response_SendPlayedCardToAllPlayers object) {
        Response_SendPlayedCardToAllPlayers response = object;

        Card cardPlayed = response.card;

        LOG.info("Played card " + cardPlayed.getSymbol().toString() + cardPlayed.getCardValue().toString() + " from player: " + response.playerID + " was received");

        //notify UI
        playedCard.postValue(object);
    }

    private void handle_SendTrumpToAllPlayers(Response_SendTrumpToAllPlayers object) {
        Response_SendTrumpToAllPlayers response = object;

        Symbol currentTrump = response.trump;

        LOG.info("Trump: " + trump.toString() + " was sent to player!");

        //notify UI
        trump.postValue(currentTrump);
    }
    private void handle_SendSchlagToAllPlayers(Response_SendSchlagToAllPlayers object){
        Response_SendSchlagToAllPlayers response = object;
        CardValue currentSchlag = response.schlag;
        LOG.info("Schlag: "+ schlag.toString() + "was sent to player!");
        schlag.postValue(currentSchlag);
    }

    private void handle_NotifyPlayerToSetSchlag(Response_NotifyPlayerToSetSchlag object) {
        Response_NotifyPlayerToSetSchlag response =
                object;

        // notify UI: to set schlag
        setSchlag.postValue(true);

        LOG.info("Please set schlag!");
    }

    private void handle_NotifyPlayerToSetTrump(Response_NotifyPlayerToSetTrump object) {
        Response_NotifyPlayerToSetTrump response =
                object;

        // notify UI: to set trump
        setTrump.postValue(true);

        LOG.info("Please set trump!");
    }

    private void handle_UpdatePointsWinnerPlayer(Response_UpdatePointsWinnerPlayer object){
        Response_UpdatePointsWinnerPlayer response = object;

        points.postValue(response.totalPoints);
    }

    private void handle_PlayerDisconnected(Response_playerDisconnected object){
        LOG.info("PLayer has disconected ID:" + object.playerID);
        //TODO: notify the UI that a player disconected from the game
    }

    //mixed cards
    private void handle_MixedCards(Response_mixedCards object){
        Response_mixedCards response = object;
        mixedCards.postValue(true);
    //
        //  mixCards();
        LOG.info("mixed cards");
    }

    private void handle_SendRoundWinnerPlayerToAllPlayers(Response_SendRoundWinnerPlayerToAllPlayers object) {
        Response_SendRoundWinnerPlayerToAllPlayers response = object;

        winnerId.postValue(response.winnerPlayerID);

        LOG.info("Player: " + response.winnerPlayerID + " has won the round!");
    }

    private void handle_UpdateScoreboard(Response_UpdateScoreboard object) {
        Response_UpdateScoreboard response = object;

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
        /*
        new Thread(()->{

            client.sendTCP(new Requests.StartGameMessage());
        }).start();
        */
        sendPacket(new Request_StartGameMessage());
    }

    public void setCard(Card card) {
        /*
        new Thread(()->{
            Requests.PlayerSetCard request = new Requests.PlayerSetCard();
            request.card =  card;
            client.sendTCP(request);
            //player should not play a card if it is not their turn
            myTurn.postValue(false);
        }).start();
        */
        Request_PlayerSetCard request = new Request_PlayerSetCard();
        request.card =  card;
        sendPacket(request);

        myTurn.postValue(false);

    }

   public void mixCards(){
        Request_MixCardsRequest request = new Request_MixCardsRequest();
        sendPacket(request);
       // mixedCards.postValue(true);

    }
    public void setSchlag(CardValue schlag) {
        Request_PlayerSetSchlag request = new Request_PlayerSetSchlag(schlag);
        sendPacket(request);
        setSchlag.postValue(false);
    }

    public void setTrump(Symbol trump) {
        Request_PlayerSetTrump request = new Request_PlayerSetTrump(trump);
        sendPacket(request);
        setTrump.postValue(false);
    }

    /////////////////// START - CHAT - LOGiC ///////////////////

    // Will be used for updating UI when Client receives Messages from Server
    private MutableLiveData<ArrayList<ChatMessage>> chatMessages;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    private void handle_ReceiveToAllChatMessage(Response_ReceiveToAllChatMessage object) {
        Response_ReceiveToAllChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);

        ArrayList<ChatMessage> value = chatMessages.getValue();
        value.add(msg);
        chatMessages.postValue(value);
    }

    private void handle_ReceiveEndToEndMessage(Response_ReceiveEndToEndChatMessage object) {
        Response_ReceiveEndToEndChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
    }

    public void sendEndToEndMessage(String message, int to) {
        Request_SendEndToEndChatMessage request = new Request_SendEndToEndChatMessage(message, playerID, to);
        sendPacket(request);
    }

    public void sendToAll(String message) {
        Request_SendToAllChatMessage request = new Request_SendToAllChatMessage(message, playerID);
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
    private MutableLiveData<Response_SendPlayedCardToAllPlayers> playedCard;
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

    public LiveData<Response_SendPlayedCardToAllPlayers> getPlayedCard(){
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
    }


    public void setSetTrump(MutableLiveData<Boolean> trump) {
        this.setTrump = trump;
    }

    public void setSetSchlag(MutableLiveData<Boolean> setSchlag) {
        this.setSchlag = setSchlag;
    }

    public void sendVoteForNextGame(GameName nextGame){
        Request_VoteForNextGame request =
                new Request_VoteForNextGame(nextGame);

        sendPacket(request);

        voteForNextGame.postValue(false);

        LOG.info("Client voted for " + nextGame.toString());
    }

    public void forceVoting(){
        Request_ForceVoting request = new Request_ForceVoting();

        sendPacket(request);

        LOG.info("Voting force message has been sent to the server");
    }

    public void cheatRequest() {
        Request_PlayerRequestsCheat request = new Request_PlayerRequestsCheat();

        sendPacket(request);

        LOG.info("CheatRequest was sent");
    }

    public void exposePossibleCheater(Integer playerId) {
        Request_ExposePossibleCheater request = new Request_ExposePossibleCheater(playerId);
        sendPacket(request);
        LOG.info("ExposePossibleCheater Request was sent for playerID : " + playerId);
    }

    public void notifyVote() {
        voteForNextGame.postValue(true);
    }

    public LiveData<Boolean> isCheatingExposed() {
        return cheatingExposed;
    }
    /////////////// END - MainGameUIs - LOGiC ///////////////
}