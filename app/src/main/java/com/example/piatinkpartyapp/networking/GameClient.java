package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
                    }
                    if (object instanceof Packets.Responses.ReceiveEndToEndChatMessage) {
                        Packets.Responses.ReceiveEndToEndChatMessage receivedMessage =
                                (Packets.Responses.ReceiveEndToEndChatMessage) object;
                        LOG.info("Client : " + playerID + " , received Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);

                        // TODO: notify UI.

                    } else if (object instanceof Packets.Responses.ReceiveToAllChatMessage) {
                        Packets.Responses.ReceiveToAllChatMessage receivedMessage =
                                (Packets.Responses.ReceiveToAllChatMessage) object;
                        LOG.info("Client : " + playerID + " , received All Message from Client : " + receivedMessage.from + " with the message : " + receivedMessage.message);
                        // TODO: notify UI.
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

}