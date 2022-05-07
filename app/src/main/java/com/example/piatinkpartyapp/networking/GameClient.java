package com.example.piatinkpartyapp.networking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.piatinkpartyapp.cards.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GameClient extends ViewModel {

    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    private static GameClient INSTANCE = null;
    private int playerID;
    private Client client;
    private ExecutorService executorService;
    int x = 11;

    private MutableLiveData<ArrayList<Card>> handCards;
    private MutableLiveData<Boolean> connectionState;
    private MutableLiveData<Boolean> myTurn;
    private MutableLiveData<Boolean> gameStarted;
    private MutableLiveData<Card> handoutCard;
    private MutableLiveData<Boolean> endOfRound;

    public GameClient(String gameServer_IP) {
        executorService = Executors.newFixedThreadPool(5);
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
        addLiveData();
    }

    private void addLiveData(){
        handCards = new MutableLiveData<>();
        connectionState = new MutableLiveData<>();
        myTurn = new MutableLiveData<>();
        gameStarted = new MutableLiveData<>();
        handoutCard = new MutableLiveData<>();
        endOfRound = new MutableLiveData<>();
    }

    public GameClient(){
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
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
        addLiveData();
    }

    public static GameClient getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new GameClient(NetworkHandler.GAMESERVER_IP);
        }
        return INSTANCE;
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
                    if (object instanceof Packets.Responses.ConnectedSuccessfully) {
                        Packets.Responses.ConnectedSuccessfully response =
                                (Packets.Responses.ConnectedSuccessfully) object;

                        //update for ui
                        connectionState.postValue(((Packets.Responses.ConnectedSuccessfully) object)    .isConnected);

                        if (response.isConnected && playerID == response.playerID) {
                            LOG.info("Client connected successfully to server : " + NetworkHandler.GAMESERVER_IP +
                                    ", Client ID within game: " + response.playerID);

                            /*
                            PlayGameFragment playGameFragment = new PlayGameFragment();
                            playGameFragment.setConnectedSuccessfuly();

                             */
                        } else {
                            LOG.info("Client cannot connect to server : " + NetworkHandler.GAMESERVER_IP);
                        }
                    }
                    else if (object instanceof Packets.Responses.ReceiveEndToEndChatMessage) {
                        Packets.Responses.ReceiveEndToEndChatMessage receivedMessage =
                                (Packets.Responses.ReceiveEndToEndChatMessage) object;
                        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
                        // TODO: notify UI.
                    } else if (object instanceof Packets.Responses.ReceiveToAllChatMessage) {
                        Packets.Responses.ReceiveToAllChatMessage receivedMessage =
                                (Packets.Responses.ReceiveToAllChatMessage) object;
                        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
                        // TODO: notify UI
                    }  else if (object instanceof Packets.Responses.GameStartedClientMessage) {
                        Packets.Responses.GameStartedClientMessage response =
                                (Packets.Responses.GameStartedClientMessage) object;

                        // notify ui: game started
                        gameStarted.postValue(true);

                        LOG.info("Game was started from host!");
                    } else if (object instanceof Packets.Responses.SendHandCards) {
                        Packets.Responses.SendHandCards response =
                                (Packets.Responses.SendHandCards) object;

                        // notify ui
                        handCards.postValue(response.cards);

                        LOG.info("Handcards received for player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.NotifyPlayerYourTurn) {
                        Packets.Responses.NotifyPlayerYourTurn response =
                                (Packets.Responses.NotifyPlayerYourTurn) object;

                        // notify ui : yourturn
                        myTurn.postValue(true);

                        LOG.info("It's your turn! player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.PlayerGetHandoutCard) {
                        Packets.Responses.PlayerGetHandoutCard response =
                                (Packets.Responses.PlayerGetHandoutCard) object;

                        Card card = response.card;
                        // notify ui with handout card
                        handoutCard.postValue(card);

                        LOG.info("Handout card received for player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.EndOfRound) {
                        Packets.Responses.EndOfRound response =
                                (Packets.Responses.EndOfRound) object;

                        //notify ui: end of round
                        endOfRound.postValue(true);
                        LOG.info("End of round!");
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
            client.sendTCP(new Packets.Requests.StartGameMessage());
        }).start();
    }

    public void setCard(Card card) {
        new Thread(()->{
            Packets.Requests.PlayerSetCard request = new Packets.Requests.PlayerSetCard();
            request.card =  card;
            client.sendTCP(request);
        }).start();
    }

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

    public LiveData<Card> getHandoutCard(){
        return handoutCard;
    }
}