package com.example.piatinkpartyapp.networking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.chat.ChatMessage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    private static GameClient INSTANCE = null;
    private int playerID;
    private Client client;
    private ExecutorService executorService;

    public GameClient(String gameServer_IP) {
        initLiveData();
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
                    if (object instanceof Packets.Responses.ConnectedSuccessfully) {
                        Packets.Responses.ConnectedSuccessfully response =
                                (Packets.Responses.ConnectedSuccessfully) object;

                        // TODO: notify UI
                        if (response.isConnected && playerID == response.playerID) {
                            LOG.info("Client connected successfully to server : " + NetworkHandler.GAMESERVER_IP +
                                    ", Client ID within game: " + response.playerID);
                        } else {
                            LOG.info("Client cannot connect to server : " + NetworkHandler.GAMESERVER_IP);
                        }
                    } else if (object instanceof Packets.Responses.ReceiveEndToEndChatMessage) {
                        Packets.Responses.ReceiveEndToEndChatMessage receivedMessage =
                                (Packets.Responses.ReceiveEndToEndChatMessage) object;
                        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);


                    } else if (object instanceof Packets.Responses.ReceiveToAllChatMessage) {
                        Packets.Responses.ReceiveToAllChatMessage receivedMessage =
                                (Packets.Responses.ReceiveToAllChatMessage) object;
                        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
                        ChatMessage msg = new ChatMessage("Player " + String.valueOf(receivedMessage.from), receivedMessage.message, receivedMessage.date, ChatMessage.MessageType.OUT);
                        newMessage.postValue(msg);


                    } else if (object instanceof Packets.Responses.SendHandCards) {
                        Packets.Responses.SendHandCards response =
                                (Packets.Responses.SendHandCards) object;

                        // TODO: notify UI.
                        LOG.info("Handcards received for player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.NotifyPlayerYourTurn) {
                        Packets.Responses.NotifyPlayerYourTurn response =
                                (Packets.Responses.NotifyPlayerYourTurn) object;

                        // TODO: notify UI
                        LOG.info("It's your turn! player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.PlayerGetHandoutCard) {
                        Packets.Responses.PlayerGetHandoutCard response =
                                (Packets.Responses.PlayerGetHandoutCard) object;

                        Card card = response.card;
                        // TODO: notify UI
                        LOG.info("Handout card received for player: " + response.playerID);
                    } else if (object instanceof Packets.Responses.EndOfRound) {
                        Packets.Responses.EndOfRound response =
                                (Packets.Responses.EndOfRound) object;

                        // TODO: notify UI
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
        executorService.execute(() -> client.sendTCP(packet));
    }

    // Call this method from client to start a game
    public void startGame() {
        client.sendTCP(new Packets.Requests.StartGameMessage());
    }

    public void setCard(Card card) {
        new Thread(() -> {
            Packets.Requests.PlayerSetCard request = new Packets.Requests.PlayerSetCard();
            request.card = card;
            client.sendTCP(request);
        }).start();
    }


    /////////////////// START - CHAT - LOGiC ///////////////////
    // Will be used for updating UI when Client receives Messages from Server
    private MutableLiveData<ChatMessage> newMessage;

    public LiveData<ChatMessage> getNewChatMessage() {
        return newMessage;
    }

    public void sendEndToEndMessage(String message, int to) {
        Packets.Requests.SendEndToEndChatMessage request = new Packets.Requests.SendEndToEndChatMessage(message, playerID, to);
        sendPacket(request);
    }

    public void sendToAll(String message) {
        Packets.Requests.SendToAllChatMessage request = new Packets.Requests.SendToAllChatMessage(message, playerID);
        sendPacket(request);
    }

    public void initLiveData() {
        newMessage = new MutableLiveData<>();
    }
    /////////////////// END - CHAT - LOGiC ///////////////////

}