package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;



public class GameServer {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    private Server server;
    private ArrayList<Connection> clients = new ArrayList<>();

    public void startNewGameServer() throws IOException {
        server = new Server();
        NetworkHandler.register(server.getKryo());
        // this line of code has to run before we start / bind / connect to the server !
        server.start();
        server.bind(NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
        startListener();
    }


    private void startListener() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                try {
                    LOG.info("Client with ID : " + connection.getID() + " just connected");

                    Packets.Responses.ConnectedSuccessfully response = new Packets.Responses.ConnectedSuccessfully();
                    response.isConnected = clients.contains(connection) ? false : clients.add(connection);
                    response.playerID = connection.getID();

                    connection.sendTCP(response);
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

                    //The Server receives the Message and decides what to do whit it
                    if(object instanceof Packets.Responses.LobbyCreatedMessage){

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }
}






