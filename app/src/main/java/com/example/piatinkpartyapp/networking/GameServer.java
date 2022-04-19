package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;

public class GameServer {
    private Server server;
    private ArrayList<Connection> clients = new ArrayList<>();


    public void startNewGameServer() throws IOException {
        server = new Server();
        NetworkHandler.register(server.getKryo());
        server.start();
        server.bind(NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
        startListener();
    }


    private void startListener() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                try {

                    super.connected(connection);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {

                } catch (Exception ex) {

                }
            }
        });
    }

}






