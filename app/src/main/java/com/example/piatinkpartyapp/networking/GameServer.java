package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.example.piatinkpartyapp.gamelogic.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;



public class GameServer {
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    private Server server;
    private ArrayList<Connection> clients = new ArrayList<>();
    private Game game;
    private ExecutorService executorService;

    public void startNewGameServer() throws IOException {
        executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            server = new Server();
            NetworkHandler.register(server.getKryo());
            // this line of code has to run before we start / bind / connect to the server !!
            server.start();
            try {
                server.bind(NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // create new Game
            game = new Game();
            startListener();
        });
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
                    if (object instanceof Packets.Requests.SendEndToEndChatMessage) {
                        // TODO handleEndToEndMessage
                    } else if (object instanceof Packets.Requests.SendToAllChatMessage) {
                        // TODO handleSendToAllChatMessage

                    } else if (object instanceof Packets.Requests.StartGameMessage){
                        game.startGame();

                        LOG.info("Game started on server : " + NetworkHandler.GAMESERVER_IP +
                                ", Client ID started the game: " + connection.getID());
                    } else if (object instanceof Packets.Requests.PlayerSetCard){
                        Packets.Requests.PlayerSetCard request =
                                (Packets.Requests.PlayerSetCard) object;

                        LOG.info("Card: " + request.card.getSymbol().toString() + request.card.getCardValue().toString() + " was set from Client ID: " + connection.getID());
                        game.setCard(connection.getID(), request.card);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }

    public void sendPacket(Connection client, IPackets packet) {
        executorService.execute(() -> client.sendTCP(packet));
    }

    public void sendPacketToAllExcept(int exceptTCP_ClientID, IPackets response) {
        executorService.execute(() -> server.sendToAllExceptTCP(exceptTCP_ClientID, response));
    }

    public void sendPacketToAll(IPackets response) {
        executorService.execute(() -> server.sendToAllTCP(response));
    }
}
