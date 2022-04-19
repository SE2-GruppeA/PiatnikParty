package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.logging.Logger;

class GameClient {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    private static GameClient INSTANCE = null;
    private int id;
    private Client client;

    private GameClient() {
        client = new Client();
        // this line of code has to run before we start / bind / connect to the server !
        NetworkHandler.register(client.getKryo());
        client.start();
        try {
            client.connect(10000, NetworkHandler.GAMESERVER_IP, NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.id = client.getID();
        startListener();


    }

    private void startListener() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}