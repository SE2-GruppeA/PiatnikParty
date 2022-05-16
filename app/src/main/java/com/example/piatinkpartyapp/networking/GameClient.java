package com.example.piatinkpartyapp.networking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.chat.ChatMessage;

import java.io.IOException;
import java.util.ArrayList;
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
                e.printStackTrace();
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
                    e.printStackTrace();
                }
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (object instanceof Responses.ConnectedSuccessfully) {
                        Responses.ConnectedSuccessfully response =
                                (Responses.ConnectedSuccessfully) object;

                        // notify UI: connection information
                        connectionState.postValue(response.isConnected);

                        if (response.isConnected && playerID == response.playerID) {
                            LOG.info("Client connected successfully to server : " + NetworkHandler.GAMESERVER_IP +
                                    ", Client ID within game: " + response.playerID);
                        } else {
                            LOG.info("Client cannot connect to server : " + NetworkHandler.GAMESERVER_IP);
                        }
                    } else if (object instanceof Responses.ReceiveEndToEndChatMessage) {
                        Responses.ReceiveEndToEndChatMessage receivedMessage =
                                (Responses.ReceiveEndToEndChatMessage) object;
                        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);


                    } else if (object instanceof Responses.ReceiveToAllChatMessage) {
                        Responses.ReceiveToAllChatMessage receivedMessage =
                                (Responses.ReceiveToAllChatMessage) object;
                        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
                        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);

                        ArrayList<ChatMessage> value = chatMessages.getValue();
                        value.add(msg);
                        chatMessages.postValue(value);

                    } else if (object instanceof Responses.GameStartedClientMessage) {
                        Responses.GameStartedClientMessage response =
                                (Responses.GameStartedClientMessage) object;

                        // notify UI: game has started
                        gameStarted.postValue(true);

                        LOG.info("Game started by server");
                    } else if (object instanceof Responses.SendHandCards) {
                        Responses.SendHandCards response =
                                (Responses.SendHandCards) object;

                        // notify UI: send handcards to SchnopsnFragment
                        handCards.postValue(response.cards);

                        LOG.info("Handcards received for player: " + response.playerID);
                    } else if (object instanceof Responses.NotifyPlayerYourTurn) {
                        Responses.NotifyPlayerYourTurn response =
                                (Responses.NotifyPlayerYourTurn) object;

                        // notify UI: its the clients turn
                        myTurn.postValue(true);

                        LOG.info("It's your turn! player: " + response.playerID);
                    } else if (object instanceof Responses.PlayerGetHandoutCard) {
                        Responses.PlayerGetHandoutCard response =
                                (Responses.PlayerGetHandoutCard) object;

                        // notify UI: clients gets one card
                        handoutCard.postValue(response.card);

                        LOG.info("Handout card received for player: " + response.playerID);
                    } else if (object instanceof Responses.EndOfRound) {
                        Responses.EndOfRound response =
                                (Responses.EndOfRound) object;

                        // notify UI: round of player is over
                        endOfRound.postValue(true);

                        LOG.info("End of round!");
                    } else if(object instanceof Responses.VoteForNextGame){
                        voteForNextGame.postValue(true);
                        LOG.info("VoteForNextGame received from the server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Generic function which should be used for sending packets to server!
    public void sendPacket(IPackets packet) {
        executorService.execute(() -> {
            client.sendTCP(packet);
        });
    }
    // Call this method from client to start a game
    public void startGame() {
        new Thread(()->{
            client.sendTCP(new Requests.StartGameMessage());
        }).start();
    }

    public void setCard(Card card) {
        new Thread(()->{
            Requests.PlayerSetCard request = new Requests.PlayerSetCard();
            request.card =  card;
            client.sendTCP(request);
            //player should not play a card if it is not their turn
            myTurn.postValue(false);
        }).start();
    }


    /////////////////// START - CHAT - LOGiC ///////////////////
    // Will be used for updating UI when Client receives Messages from Server
    private MutableLiveData<ArrayList<ChatMessage>> chatMessages;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return chatMessages;
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
        dummy.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        dummy.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        dummy.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
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
    private MutableLiveData<Boolean> endOfRound;
    private MutableLiveData<Boolean> voteForNextGame;

    public LiveData<Boolean> getConnectionState(){
        return connectionState;
    }

    public LiveData<ArrayList<Card>> getHandCards(){
        return handCards;
    }

    public LiveData<Boolean> isMyTurn(){
        return myTurn;
    }

    public LiveData<Boolean> isGameStarted(){
        return gameStarted;
    }

    public LiveData<Boolean> isEndOfRound() {
        return endOfRound;
    }

    public LiveData<Card> getHandoutCard(){
        return handoutCard;
    }

    public LiveData<Boolean> isVotingForNextGame() { return voteForNextGame; }

    private void initLiveDataMainGameUIs(){
        handCards = new MutableLiveData<>();
        connectionState = new MutableLiveData<>();
        myTurn = new MutableLiveData<>();
        gameStarted = new MutableLiveData<>();
        handoutCard = new MutableLiveData<>();
        endOfRound = new MutableLiveData<>();
        voteForNextGame = new MutableLiveData<>();
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

    /////////////// END - MainGameUIs - LOGiC ///////////////
}