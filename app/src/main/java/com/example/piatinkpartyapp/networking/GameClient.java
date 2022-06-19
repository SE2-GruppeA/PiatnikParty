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
import com.example.piatinkpartyapp.networking.Responses.CheatingPenalty;
import com.example.piatinkpartyapp.networking.Responses.ConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.Responses.EndOfRound;
import com.example.piatinkpartyapp.networking.Responses.GameStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.HosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.IsCheater;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.Responses.PensionistlnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.PlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.Responses.ReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Responses.ReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.Responses.SchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.SendHandCards;
import com.example.piatinkpartyapp.networking.Responses.SendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.UpdateScoreboard;
import com.example.piatinkpartyapp.networking.Responses.WattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.playerDisconnected;
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
                    if (object instanceof ConnectedSuccessfully) {
                        handle_ConnectedSuccessfully((ConnectedSuccessfully) object);
                    } else if (object instanceof ReceiveEndToEndChatMessage) {
                        handle_ReceiveEndToEndMessage((ReceiveEndToEndChatMessage) object);
                    } else if (object instanceof ReceiveToAllChatMessage) {
                        handle_ReceiveToAllChatMessage((ReceiveToAllChatMessage) object);
                    } else if (object instanceof GameStartedClientMessage) {
                        handle_GameStartedClientMessage((GameStartedClientMessage) object);
                    } else if (object instanceof SendHandCards) {
                        handle_SendHandCards((SendHandCards) object);
                    } else if (object instanceof NotifyPlayerYourTurn) {
                        handle_NotifyPlayerYourTurn((NotifyPlayerYourTurn) object);
                    } else if (object instanceof PlayerGetHandoutCard) {
                        handle_PlayerGetHandoutCard((PlayerGetHandoutCard) object);
                    } else if (object instanceof EndOfRound) {
                        handle_EndOfRound((EndOfRound) object);
                    } else if (object instanceof com.example.piatinkpartyapp.networking.Responses.VoteForNextGame){
                        handle_VoteForNextGame();
                    } else if (object instanceof SendPlayedCardToAllPlayers) {
                        handle_SendPlayedCardToAllPlayers((SendPlayedCardToAllPlayers) object);
                    } else if (object instanceof SendTrumpToAllPlayers) {
                        handle_SendTrumpToAllPlayers((SendTrumpToAllPlayers) object);
                    } else if (object instanceof NotifyPlayerToSetSchlag) {
                        handle_NotifyPlayerToSetSchlag((NotifyPlayerToSetSchlag) object);
                    } else if (object instanceof NotifyPlayerToSetTrump) {
                        handle_NotifyPlayerToSetTrump((NotifyPlayerToSetTrump) object);
                    } else if(object instanceof UpdatePointsWinnerPlayer){
                        handle_UpdatePointsWinnerPlayer((UpdatePointsWinnerPlayer) object);
                    } else if (object instanceof SchnopsnStartedClientMessage) {
                        handle_SchnopsnStartedClientMessage((SchnopsnStartedClientMessage) object);
                    } else if (object instanceof WattnStartedClientMessage){
                        handle_WattnStartedClientMessage((WattnStartedClientMessage) object);
                    } else if (object instanceof PensionistlnStartedClientMessage){
                        handle_PensionistlnStartedClientMessage((PensionistlnStartedClientMessage) object);
                    } else if (object instanceof HosnObeStartedClientMessage){
                        handle_HosnObeStartedClientMessage((HosnObeStartedClientMessage) object);
                    }else if(object instanceof playerDisconnected){
                        handle_PlayerDisconnected((playerDisconnected)object);
                    }else if(object instanceof com.example.piatinkpartyapp.networking.Responses.mixedCards){
                        handle_MixedCards((com.example.piatinkpartyapp.networking.Responses.mixedCards)object);
                    }else if(object instanceof IsCheater){
                        handle_isCheater((IsCheater)object);
                    } else if (object instanceof SendRoundWinnerPlayerToAllPlayers) {
                        handle_SendRoundWinnerPlayerToAllPlayers((SendRoundWinnerPlayerToAllPlayers) object);
                    } else if (object instanceof UpdateScoreboard) {
                        handle_UpdateScoreboard((UpdateScoreboard) object);
                    } else if(object instanceof CheatingPenalty){
                        handle_CheatingPenalty((CheatingPenalty) object);
                    }else if(object instanceof SendSchlagToAllPlayers){
                        handle_SendSchlagToAllPlayers((SendSchlagToAllPlayers) object);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.info(e.toString());
                }
            }
        });
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_isCheater(IsCheater object) {
        // todo: handle live data and update ui
        boolean isCheater = object.isCheater;

        cheaterExposed.postValue(isCheater);
        /**
         * If isCheater == true, einfach normal anzeigen das man einen cheater exposed hat
         * If false, anzeigen das man -10 punkte verloren hat.
         */
    }

    private void handle_CheatingPenalty(CheatingPenalty object) {
        cheatingExposed.postValue(true);
    }

    private void handle_VoteForNextGame() {
        voteForNextGame.postValue(true);
        LOG.info("VoteForNextGame received from the server");
    }

    private void handle_EndOfRound(EndOfRound object) {
        EndOfRound response =
                object;

        // notify UI: round of player is over
        endOfRound.postValue(response.playerID);

        LOG.info("End of round!");
    }

    private void handle_PlayerGetHandoutCard(PlayerGetHandoutCard object) {
        PlayerGetHandoutCard response =
                object;

        // notify UI: clients gets one card
        handoutCard.postValue(response.card);

        LOG.info("Handout card received for player: " + response.playerID);
    }

    private void handle_NotifyPlayerYourTurn(NotifyPlayerYourTurn object) {
        NotifyPlayerYourTurn response =
                object;

        // notify UI: its the clients turn
        myTurn.postValue(true);

        LOG.info("It's your turn! player: " + response.playerID);
    }

    private void handle_SendHandCards(SendHandCards object) {
        SendHandCards response =
                object;

        // notify UI: send handcards to SchnopsnFragment
        handCards.postValue(response.cards);

        LOG.info("Handcards received for player: " + response.playerID);
    }

    private void handle_GameStartedClientMessage(GameStartedClientMessage object) {
        GameStartedClientMessage response =
                object;

        // notify UI: game has started
        gameStarted.postValue(true);

        LOG.info("Game started by server");
    }

    private void handle_SchnopsnStartedClientMessage(SchnopsnStartedClientMessage object) {
        SchnopsnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(true);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Schnopsn Game started by server after voting");
    }

    private void handle_WattnStartedClientMessage(WattnStartedClientMessage object) {
        WattnStartedClientMessage response =
                object;

        // notify UI: game has started
        wattnStarted.postValue(true);
        schnopsnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Wattn Game started by server after voting");
    }

    private void handle_PensionistlnStartedClientMessage(PensionistlnStartedClientMessage object) {
        PensionistlnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(true);
        hosnObeStarted.postValue(false);

        LOG.info("Pensionistln Game started by server after voting");
    }

    private void handle_HosnObeStartedClientMessage(HosnObeStartedClientMessage object) {
        HosnObeStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(true);

        LOG.info("Hosn Obe Game started by server after voting");
    }

    private void handle_ConnectedSuccessfully(ConnectedSuccessfully object) {
        ConnectedSuccessfully response =
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

    private void handle_SendPlayedCardToAllPlayers(SendPlayedCardToAllPlayers object) {
        SendPlayedCardToAllPlayers response = object;

        Card cardPlayed = response.card;

        LOG.info("Played card " + cardPlayed.getSymbol().toString() + cardPlayed.getCardValue().toString() + " from player: " + response.playerID + " was received");

        //notify UI
        playedCard.postValue(object);
    }

    private void handle_SendTrumpToAllPlayers(SendTrumpToAllPlayers object) {
        SendTrumpToAllPlayers response = object;

        Symbol currentTrump = response.trump;

        LOG.info("Trump: " + trump.toString() + " was sent to player!");

        //notify UI
        trump.postValue(currentTrump);
    }
    private void handle_SendSchlagToAllPlayers(SendSchlagToAllPlayers object){
        SendSchlagToAllPlayers response = object;
        CardValue currentSchlag = response.schlag;
        LOG.info("Schlag: "+ schlag.toString() + "was sent to player!");
        schlag.postValue(currentSchlag);
    }

    private void handle_NotifyPlayerToSetSchlag(NotifyPlayerToSetSchlag object) {
        NotifyPlayerToSetSchlag response =
                object;

        // notify UI: to set schlag
        setSchlag.postValue(true);

        LOG.info("Please set schlag!");
    }

    private void handle_NotifyPlayerToSetTrump(NotifyPlayerToSetTrump object) {
        NotifyPlayerToSetTrump response =
                object;

        // notify UI: to set trump
        setTrump.postValue(true);

        LOG.info("Please set trump!");
    }

    private void handle_UpdatePointsWinnerPlayer(UpdatePointsWinnerPlayer object){
        UpdatePointsWinnerPlayer response = object;

        points.postValue(response.totalPoints);
    }

    private void handle_PlayerDisconnected(playerDisconnected object){
        LOG.info("PLayer has disconected ID:" + object.playerID);
        //TODO: notify the UI that a player disconected from the game
    }

    //mixed cards
    private void handle_MixedCards(com.example.piatinkpartyapp.networking.Responses.mixedCards object){
        com.example.piatinkpartyapp.networking.Responses.mixedCards response = object;
        mixedCards.postValue(true);
    //
        //  mixCards();
        LOG.info("mixed cards");
    }

    private void handle_SendRoundWinnerPlayerToAllPlayers(SendRoundWinnerPlayerToAllPlayers object) {
        SendRoundWinnerPlayerToAllPlayers response = object;

        winnerId.postValue(response.winnerPlayerID);

        LOG.info("Player: " + response.winnerPlayerID + " has won the round!");
    }

    private void handle_UpdateScoreboard(UpdateScoreboard object) {
        UpdateScoreboard response = object;

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

    private void handle_ReceiveToAllChatMessage(ReceiveToAllChatMessage object) {
        ReceiveToAllChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);

        ArrayList<ChatMessage> value = chatMessages.getValue();
        value.add(msg);
        chatMessages.postValue(value);
    }

    private void handle_ReceiveEndToEndMessage(ReceiveEndToEndChatMessage object) {
        ReceiveEndToEndChatMessage receivedMessage =
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
    private MutableLiveData<SendPlayedCardToAllPlayers> playedCard;
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

    public LiveData<SendPlayedCardToAllPlayers> getPlayedCard(){
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