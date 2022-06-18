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
                    if (object instanceof Responses.ConnectedSuccessfully) {
                        handle_ConnectedSuccessfully((Responses.ConnectedSuccessfully) object);
                    } else if (object instanceof Responses.ReceiveEndToEndChatMessage) {
                        handle_ReceiveEndToEndMessage((Responses.ReceiveEndToEndChatMessage) object);
                    } else if (object instanceof Responses.ReceiveToAllChatMessage) {
                        handle_ReceiveToAllChatMessage((Responses.ReceiveToAllChatMessage) object);
                    } else if (object instanceof Responses.GameStartedClientMessage) {
                        handle_GameStartedClientMessage((Responses.GameStartedClientMessage) object);
                    } else if (object instanceof Responses.SendHandCards) {
                        handle_SendHandCards((Responses.SendHandCards) object);
                    } else if (object instanceof Responses.NotifyPlayerYourTurn) {
                        handle_NotifyPlayerYourTurn((Responses.NotifyPlayerYourTurn) object);
                    } else if (object instanceof Responses.PlayerGetHandoutCard) {
                        handle_PlayerGetHandoutCard((Responses.PlayerGetHandoutCard) object);
                    } else if (object instanceof Responses.EndOfRound) {
                        handle_EndOfRound((Responses.EndOfRound) object);
                    } else if (object instanceof Responses.VoteForNextGame){
                        handle_VoteForNextGame();
                    } else if (object instanceof Responses.SendPlayedCardToAllPlayers) {
                        handle_SendPlayedCardToAllPlayers((Responses.SendPlayedCardToAllPlayers) object);
                    } else if (object instanceof Responses.SendTrumpToAllPlayers) {
                        handle_SendTrumpToAllPlayers((Responses.SendTrumpToAllPlayers) object);
                    } else if (object instanceof Responses.NotifyPlayerToSetSchlag) {
                        handle_NotifyPlayerToSetSchlag((Responses.NotifyPlayerToSetSchlag) object);
                    } else if (object instanceof Responses.NotifyPlayerToSetTrump) {
                        handle_NotifyPlayerToSetTrump((Responses.NotifyPlayerToSetTrump) object);
                    } else if(object instanceof Responses.UpdatePointsWinnerPlayer){
                        handle_UpdatePointsWinnerPlayer((Responses.UpdatePointsWinnerPlayer) object);
                    } else if (object instanceof Responses.SchnopsnStartedClientMessage) {
                        handle_SchnopsnStartedClientMessage((Responses.SchnopsnStartedClientMessage) object);
                    } else if (object instanceof Responses.WattnStartedClientMessage){
                        handle_WattnStartedClientMessage((Responses.WattnStartedClientMessage) object);
                    } else if (object instanceof Responses.PensionistlnStartedClientMessage){
                        handle_PensionistlnStartedClientMessage((Responses.PensionistlnStartedClientMessage) object);
                    } else if (object instanceof Responses.HosnObeStartedClientMessage){
                        handle_HosnObeStartedClientMessage((Responses.HosnObeStartedClientMessage) object);
                    }else if(object instanceof Responses.playerDisconnected){
                        handle_PlayerDisconnected((Responses.playerDisconnected)object);
                    }else if(object instanceof  Responses.mixedCards){
                        handle_MixedCards((Responses.mixedCards)object);
                    }else if(object instanceof  Responses.IsCheater){
                        handle_isCheater((Responses.IsCheater)object);
                    } else if (object instanceof Responses.SendRoundWinnerPlayerToAllPlayers) {
                        handle_SendRoundWinnerPlayerToAllPlayers((Responses.SendRoundWinnerPlayerToAllPlayers) object);
                    } else if (object instanceof Responses.UpdateScoreboard) {
                        handle_UpdateScoreboard((Responses.UpdateScoreboard) object);
                    } else if(object instanceof Responses.CheatingPenalty){
                        handle_CheatingPenalty((Responses.CheatingPenalty) object);
                    }else if(object instanceof  Responses.SendSchlagToAllPlayers){
                        handle_SendSchlagToAllPlayers((Responses.SendSchlagToAllPlayers) object);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.info(e.toString());
                }
            }
        });
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_isCheater(Responses.IsCheater object) {
        // todo: handle live data and update ui
        boolean isCheater = object.isCheater;

        cheaterExposed.postValue(isCheater);
        /**
         * If isCheater == true, einfach normal anzeigen das man einen cheater exposed hat
         * If false, anzeigen das man -10 punkte verloren hat.
         */
    }

    private void handle_CheatingPenalty(Responses.CheatingPenalty object) {
        cheatingExposed.postValue(true);
    }

    private void handle_VoteForNextGame() {
        voteForNextGame.postValue(true);
        LOG.info("VoteForNextGame received from the server");
    }

    private void handle_EndOfRound(Responses.EndOfRound object) {
        Responses.EndOfRound response =
                object;

        // notify UI: round of player is over
        endOfRound.postValue(response.playerID);

        LOG.info("End of round!");
    }

    private void handle_PlayerGetHandoutCard(Responses.PlayerGetHandoutCard object) {
        Responses.PlayerGetHandoutCard response =
                object;

        // notify UI: clients gets one card
        handoutCard.postValue(response.card);

        LOG.info("Handout card received for player: " + response.playerID);
    }

    private void handle_NotifyPlayerYourTurn(Responses.NotifyPlayerYourTurn object) {
        Responses.NotifyPlayerYourTurn response =
                object;

        // notify UI: its the clients turn
        myTurn.postValue(true);

        LOG.info("It's your turn! player: " + response.playerID);
    }

    private void handle_SendHandCards(Responses.SendHandCards object) {
        Responses.SendHandCards response =
                object;

        // notify UI: send handcards to SchnopsnFragment
        handCards.postValue(response.cards);

        LOG.info("Handcards received for player: " + response.playerID);
    }

    private void handle_GameStartedClientMessage(Responses.GameStartedClientMessage object) {
        Responses.GameStartedClientMessage response =
                object;

        // notify UI: game has started
        gameStarted.postValue(true);

        LOG.info("Game started by server");
    }

    private void handle_SchnopsnStartedClientMessage(Responses.SchnopsnStartedClientMessage object) {
        Responses.SchnopsnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(true);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Schnopsn Game started by server after voting");
    }

    private void handle_WattnStartedClientMessage(Responses.WattnStartedClientMessage object) {
        Responses.WattnStartedClientMessage response =
                object;

        // notify UI: game has started
        wattnStarted.postValue(true);
        schnopsnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(false);

        LOG.info("Wattn Game started by server after voting");
    }

    private void handle_PensionistlnStartedClientMessage(Responses.PensionistlnStartedClientMessage object) {
        Responses.PensionistlnStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(true);
        hosnObeStarted.postValue(false);

        LOG.info("Pensionistln Game started by server after voting");
    }

    private void handle_HosnObeStartedClientMessage(Responses.HosnObeStartedClientMessage object) {
        Responses.HosnObeStartedClientMessage response =
                object;

        // notify UI: game has started
        schnopsnStarted.postValue(false);
        wattnStarted.postValue(false);
        pensionistlnStarted.postValue(false);
        hosnObeStarted.postValue(true);

        LOG.info("Hosn Obe Game started by server after voting");
    }

    private void handle_ConnectedSuccessfully(Responses.ConnectedSuccessfully object) {
        Responses.ConnectedSuccessfully response =
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

    private void handle_SendPlayedCardToAllPlayers(Responses.SendPlayedCardToAllPlayers object) {
        Responses.SendPlayedCardToAllPlayers response = object;

        Card cardPlayed = response.card;

        LOG.info("Played card " + cardPlayed.getSymbol().toString() + cardPlayed.getCardValue().toString() + " from player: " + response.playerID + " was received");

        //notify UI
        playedCard.postValue(object);
    }

    private void handle_SendTrumpToAllPlayers(Responses.SendTrumpToAllPlayers object) {
        Responses.SendTrumpToAllPlayers response = object;

        Symbol currentTrump = response.trump;

        LOG.info("Trump: " + trump.toString() + " was sent to player!");

        //notify UI
        trump.postValue(currentTrump);
    }
    private void handle_SendSchlagToAllPlayers(Responses.SendSchlagToAllPlayers object){
        Responses.SendSchlagToAllPlayers response = object;
        CardValue currentSchlag = response.schlag;
        LOG.info("Schlag: "+ schlag.toString() + "was sent to player!");
        schlag.postValue(currentSchlag);
    }

    private void handle_NotifyPlayerToSetSchlag(Responses.NotifyPlayerToSetSchlag object) {
        Responses.NotifyPlayerToSetSchlag response =
                object;

        // notify UI: to set schlag
        setSchlag.postValue(true);

        LOG.info("Please set schlag!");
    }

    private void handle_NotifyPlayerToSetTrump(Responses.NotifyPlayerToSetTrump object) {
        Responses.NotifyPlayerToSetTrump response =
                object;

        // notify UI: to set trump
        setTrump.postValue(true);

        LOG.info("Please set trump!");
    }

    private void handle_UpdatePointsWinnerPlayer(Responses.UpdatePointsWinnerPlayer object){
        Responses.UpdatePointsWinnerPlayer response = object;

        points.postValue(response.totalPoints);
    }

    private void handle_PlayerDisconnected(Responses.playerDisconnected object){
        LOG.info("PLayer has disconected ID:" + object.playerID);
        //TODO: notify the UI that a player disconected from the game
    }

    //mixed cards
    private void handle_MixedCards(Responses.mixedCards object){
        Responses.mixedCards response = object;
        mixedCards.postValue(true);
    //
        //  mixCards();
        LOG.info("mixed cards");
    }

    private void handle_SendRoundWinnerPlayerToAllPlayers(Responses.SendRoundWinnerPlayerToAllPlayers object) {
        Responses.SendRoundWinnerPlayerToAllPlayers response = object;

        winnerId.postValue(response.winnerPlayerID);

        LOG.info("Player: " + response.winnerPlayerID + " has won the round!");
    }

    private void handle_UpdateScoreboard(Responses.UpdateScoreboard object) {
        Responses.UpdateScoreboard response = object;

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
        sendPacket(new Requests.StartGameMessage());
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
        Requests.PlayerSetCard request = new Requests.PlayerSetCard();
        request.card =  card;
        sendPacket(request);

        myTurn.postValue(false);

    }

   public void mixCards(){
        Requests.MixCardsRequest request = new Requests.MixCardsRequest();
        sendPacket(request);
       // mixedCards.postValue(true);

    }
    public void setSchlag(CardValue schlag) {
        Requests.PlayerSetSchlag request = new Requests.PlayerSetSchlag(schlag);
        sendPacket(request);
        setSchlag.postValue(false);
    }

    public void setTrump(Symbol trump) {
        Requests.PlayerSetTrump request = new Requests.PlayerSetTrump(trump);
        sendPacket(request);
        setTrump.postValue(false);
    }

    /////////////////// START - CHAT - LOGiC ///////////////////

    // Will be used for updating UI when Client receives Messages from Server
    private MutableLiveData<ArrayList<ChatMessage>> chatMessages;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    private void handle_ReceiveToAllChatMessage(Responses.ReceiveToAllChatMessage object) {
        Responses.ReceiveToAllChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);

        ArrayList<ChatMessage> value = chatMessages.getValue();
        value.add(msg);
        chatMessages.postValue(value);
    }

    private void handle_ReceiveEndToEndMessage(Responses.ReceiveEndToEndChatMessage object) {
        Responses.ReceiveEndToEndChatMessage receivedMessage =
                object;
        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
    }

    public void sendEndToEndMessage(String message, int to) {
        Requests.SendEndToEndChatMessage request = new Requests.SendEndToEndChatMessage(message, playerID, to);
        sendPacket(request);
    }

    public void sendToAll(String message) {
        Requests.SendToAllChatMessage request = new Requests.SendToAllChatMessage(message, playerID);
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
    private MutableLiveData<Responses.SendPlayedCardToAllPlayers> playedCard;
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

    public LiveData<Responses.SendPlayedCardToAllPlayers> getPlayedCard(){
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
        Requests.VoteForNextGame request =
                new Requests.VoteForNextGame(nextGame);

        sendPacket(request);

        voteForNextGame.postValue(false);

        LOG.info("Client voted for " + nextGame.toString());
    }

    public void forceVoting(){
        Requests.ForceVoting request = new Requests.ForceVoting();

        sendPacket(request);

        LOG.info("Voting force message has been sent to the server");
    }

    public void cheatRequest() {
        Requests.PlayerRequestsCheat request = new Requests.PlayerRequestsCheat();

        sendPacket(request);

        LOG.info("CheatRequest was sent");
    }

    public void exposePossibleCheater(Integer playerId) {
        Requests.ExposePossibleCheater request = new Requests.ExposePossibleCheater(playerId);
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