package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    private static GameClient INSTANCE = null;
    private int playerID;
    private Client client;

    public GameClient() {

        // we to start this in a new thread, so we don't block the main Thread!
        new Thread(()->{
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
        }).start();
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
                    else if(object instanceof Packets.Responses.LobbyCreatedMessage){

                    } else if (object instanceof Packets.Responses.SendHandCards) {
                        Packets.Responses.SendHandCards response =
                                (Packets.Responses.SendHandCards) object;

                        // TODO: notify UI
                        LOG.info("Handcards received for player: " + response.playerID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //This methods send new packets to Server
    public void createLobby(){
        client.sendTCP(new Packets.Responses.LobbyCreatedMessage());
    }

}