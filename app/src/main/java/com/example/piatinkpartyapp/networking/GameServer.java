package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                    handle_connected(connection);
                    super.connected(connection);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                handle_disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (object instanceof Requests.SendEndToEndChatMessage) {
                        handleEndToEndMessage((Requests.SendEndToEndChatMessage) object);
                    } else if (object instanceof Requests.SendToAllChatMessage) {
                        handleSendToAllChatMessage((Requests.SendToAllChatMessage) object);
                    } else if (object instanceof Requests.StartGameMessage) {
                        handle_StartGameMessage(connection);
                    } else if (object instanceof Requests.PlayerSetCard) {
                        handle_PlayerSetCard(connection, (Requests.PlayerSetCard) object);
                    } else if (object instanceof Requests.ForceVoting){
                        handle_ForceVoting(connection);
                    } else if(object instanceof  Requests.VoteForNextGame){
                        handle_VoteForNextGame(connection, (Requests.VoteForNextGame) object);
                    } else if (object instanceof Requests.PlayerSetSchlag) {
                        handle_PlayerSetSchlag(connection, (Requests.PlayerSetSchlag) object);
                    } else if (object instanceof Requests.PlayerSetTrump) {
                        handle_PlayerSetTrump(connection, (Requests.PlayerSetTrump) object);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_connected(Connection connection) {
        LOG.info("Client with ID : " + connection.getID() + " just connected");

        Responses.ConnectedSuccessfully response = new Responses.ConnectedSuccessfully();
        response.isConnected = clients.contains(connection) ? false : clients.add(connection);
        response.playerID = connection.getID();

        game.addPlayer(connection, "test");

        connection.sendTCP(response);

        //TODO: update teilnehmerliste (in clients stehen alle verbundenen clients)
    }

    private void handle_disconnected(Connection connection) {
        //TODO: update teilnehmerliste (in clients stehen alle verbundenen clients)
    }

    private void handle_VoteForNextGame(Connection connection, Requests.VoteForNextGame object) {
        LOG.info("Client " + connection.getID() + " voted for" +
                object.gameName.toString());
    }

    private void handle_ForceVoting(Connection connection) {
        LOG.info("Voting has been initiated by client " + connection.getID());

        Responses.VoteForNextGame response =
                new Responses.VoteForNextGame();

        sendPacketToAll(response);

        LOG.info("VoteForNextGame sent to all Clients");
    }

    private void handle_PlayerSetCard(Connection connection, Requests.PlayerSetCard object) {
        Requests.PlayerSetCard request =
                object;

        LOG.info("Card: " + request.card.getSymbol().toString() + request.card.getCardValue().toString() + " was set from Client ID: " + connection.getID());
        game.setCard(connection.getID(), request.card);
    }

    private void handle_StartGameMessage(Connection connection) {
        game.startGame();

        LOG.info("Game started on server : " + NetworkHandler.GAMESERVER_IP +
                ", Client ID started the game: " + connection.getID());
    }

    private void handle_PlayerSetSchlag(Connection connection, Requests.PlayerSetSchlag object) {
        Requests.PlayerSetSchlag request =
                object;

        LOG.info("Schlag: " + request.schlag.toString() + " was set from Client ID: " + connection.getID());
    }

    private void handle_PlayerSetTrump(Connection connection, Requests.PlayerSetTrump object) {
        Requests.PlayerSetTrump request =
                object;

        LOG.info("Trump: " + request.trump.toString() + " was set from Client ID: " + connection.getID());
    }
    /////////////////// END - Handler Methods !!! ///////////////////


    /////////////////// Chat - Handler Methods !!! ///////////////////
    private void handleEndToEndMessage(Requests.SendEndToEndChatMessage request) throws Exception {
        final Connection messageReceiverClientConnection = Arrays
                .stream(server.getConnections())
                .filter(connection -> connection.getID() == request.to)
                .findFirst()
                .orElseThrow(() -> new Exception("Client with ID : " + request.to + " not found, so we cannot send the message!"));
        Responses.ReceiveEndToEndChatMessage response
                = new Responses.ReceiveEndToEndChatMessage(request.message, request.from, request.to);
        messageReceiverClientConnection.sendTCP(response);
    }

    private void handleSendToAllChatMessage(Requests.SendToAllChatMessage request) {
        IPackets response =
                new Responses.ReceiveToAllChatMessage(request.message, request.from, Utils.getDateAsString());

        // this should be called but for testing purposes, I send to myself again, so I can see that it really worked!
        //sendPacketToAllExcept(request.from, response);
        // this line of code down below is just used for testing purposes
        //sendPacketToAll(response);

        sendPacketToAllExcept(request.from, response);
    }
    /////////////////// END - Chat - Handler Methods !!! ///////////////////



    /////////////////// START - Generic Send Methods! ///////////////////
    public void sendPacket(Connection client, IPackets packet) {
        executorService.execute(() -> client.sendTCP(packet));
    }

    public void sendPacketToAllExcept(int exceptTCP_ClientID, IPackets response) {
        executorService.execute(() -> server.sendToAllExceptTCP(exceptTCP_ClientID, response));
    }

    public void sendPacketToAll(IPackets response) {
        executorService.execute(() -> server.sendToAllTCP(response));
    }
    /////////////////// END - Generic Send Methods! ///////////////////
}
