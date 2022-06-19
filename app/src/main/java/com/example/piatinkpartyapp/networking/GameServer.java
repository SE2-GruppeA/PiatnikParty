package com.example.piatinkpartyapp.networking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.WattnGame;
import com.example.piatinkpartyapp.networking.Responses.ConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.Responses.GameStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.IsCheater;
import com.example.piatinkpartyapp.networking.Responses.ReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Responses.ReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.Responses.mixedCards;
import com.example.piatinkpartyapp.networking.Responses.playerDisconnected;
import com.example.piatinkpartyapp.networking.Requests.Request_ExposePossibleCheater;
import com.example.piatinkpartyapp.networking.Requests.Request_ForceVoting;
import com.example.piatinkpartyapp.networking.Requests.Request_MixCardsRequest;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetCard;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetSchlag;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetTrump;
import com.example.piatinkpartyapp.networking.Requests.Request_SendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_SendToAllChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_StartGameMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_VoteForNextGame;
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
    private Lobby lobby;
    private Game game;
    private WattnGame wattnGame;
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
                //e.printStackTrace();
                LOG.info(e.toString());
            }
            // create new Game
            game = new Game();

            lobby = new Lobby();
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
                    if (object instanceof Request_SendEndToEndChatMessage) {
                        handleEndToEndMessage((Request_SendEndToEndChatMessage) object);
                    } else if (object instanceof Request_SendToAllChatMessage) {
                        handleSendToAllChatMessage((Request_SendToAllChatMessage) object);
                    } else if (object instanceof Request_StartGameMessage) {
                        handle_StartGameMessage(connection);
                    } else if (object instanceof Request_PlayerSetCard) {
                        handle_PlayerSetCard(connection, (Request_PlayerSetCard) object);
                    } else if (object instanceof Request_ForceVoting){
                        handle_ForceVoting(connection);
                    } else if(object instanceof Request_VoteForNextGame){
                        handle_VoteForNextGame(connection, (Request_VoteForNextGame) object);
                    } else if (object instanceof Request_PlayerSetSchlag) {
                        handle_PlayerSetSchlag(connection, (Request_PlayerSetSchlag) object);
                    } else if (object instanceof Request_PlayerSetTrump) {
                        handle_PlayerSetTrump(connection, (Request_PlayerSetTrump) object);
                    } else if(object instanceof Request_PlayerRequestsCheat){
                        handle_PlayerRequestsCheat(connection, (Request_PlayerRequestsCheat) object);
                    }else if(object instanceof Request_MixCardsRequest){
                        handle_MixCardsRequest(connection,(Request_MixCardsRequest) object);
                    }else if(object instanceof Request_ExposePossibleCheater){
                        handle_exposePossibleCheater(connection,(Request_ExposePossibleCheater) object);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }

    private void handle_exposePossibleCheater(Connection connection, Request_ExposePossibleCheater object) {
        Integer playerId = object.playerId;

        IsCheater response = new IsCheater();
        if(isCheater(playerId, connection.getID())){
            response.isCheater = true;
        }else{
            response.isCheater = false;
        }
        connection.sendTCP(response);
    }

    //todo: Add this function to gamelogic itself, it's just here so i can build the handler above !
    private boolean isCheater(Integer playerId, Integer exposerId) {
        // todo: Implement gamelogic: how explained down below (Maybe Anton or Bene)!
        // todo: Also add live data !

        Boolean isCheater = lobby.currentGame.isPlayerCheater(playerId);

        /**
         * gameLogic.exposePossibleCheater(playerId)
         *
         * return true if player really cheater with response isCheater(true);
         * also trigger cheater to lose -20 points (he got +10 when cheating, so he is only losing -10)
         *
         * return false, and also trigger a lose -10 points with response isCheater(false)
         * comes with risks !
         */
        if(isCheater){
            lobby.currentGame.cheaterPenalty(playerId);
        }else {
            lobby.currentGame.punishWrongExposure(exposerId);
        }

        return isCheater;
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_connected(Connection connection) {
        LOG.info("Client with ID : " + connection.getID() + " just connected");

        ConnectedSuccessfully response = new ConnectedSuccessfully();
        response.isConnected = clients.contains(connection) ? false : clients.add(connection);
        response.playerID = connection.getID();

        lobby.addPlayer(connection, "Player " + connection.getID());
      //  wattnGame.addPlayer(connection, "test");
        connection.sendTCP(response);

        //update teilnehmerliste (in clients stehen alle verbundenen clients)
        players.postValue(lobby.getPlayers());
        //players.postValue(wattnGame.getPlayers());
    }

    private void handle_disconnected(Connection connection) {
        //update teilnehmerliste (in clients stehen alle verbundenen clients)
        players.postValue(lobby.getPlayers());

        //When the Player disconnects the message is send to all other players.
        playerDisconnected response = new playerDisconnected();
        response.playerID = connection.getID();
        sendPacketToAll(response);

        //players.postValue(wattnGame.getPlayers());
    }

    private void handle_VoteForNextGame(Connection connection, Request_VoteForNextGame object) {
        LOG.info("Client " + connection.getID() + " voted for" +
                object.gameName.toString());

        lobby.handleVotingForNextGame(connection.getID(), object.gameName);
       // wattnGame.handleVotingForNextGame(connection.getID(),object.gameName);
    }

    private void handle_ForceVoting(Connection connection) {
        LOG.info("Voting has been initiated by client " + connection.getID());

        com.example.piatinkpartyapp.networking.Responses.VoteForNextGame response =
                new com.example.piatinkpartyapp.networking.Responses.VoteForNextGame();

        sendPacketToAll(response);

        LOG.info("VoteForNextGame sent to all Clients");
    }

    private void handle_PlayerSetCard(Connection connection, Request_PlayerSetCard object) {
        Request_PlayerSetCard request =
                object;

        LOG.info("Card: " + request.card.getSymbol().toString() + request.card.getCardValue().toString() + " was set from Client ID: " + connection.getID());
        lobby.currentGame.setCard(connection.getID(), request.card);
    //    wattnGame.setCard(connection.getID(),request.card);
    }

    private void handle_StartGameMessage(Connection connection) {

        // zu testen, danach soll nur Tisch geöffnet werden
        //lobby.currentGame = new SchnopsnGame(lobby);
        //lobby.currentGame.startGameSchnopsn();
      //  wattnGame.startGameWattn();

        LOG.info("Game started on server : " + NetworkHandler.GAMESERVER_IP +
                ", Client ID started the game: " + connection.getID());

        // Message to all Players that game has started to open the gamefragment in order to open voting
        GameStartedClientMessage response = new GameStartedClientMessage();
        sendPacketToAll(response);

        // Message to all Players to open the voting
        handle_ForceVoting(connection);
    }

    private void handle_PlayerSetSchlag(Connection connection, Request_PlayerSetSchlag object) {
        Request_PlayerSetSchlag request =
                object;
        lobby.currentGame.setSchlag(request.schlag);

        LOG.info("Schlag: " + lobby.currentGame.getSchlag() + " was set from Client ID: " + connection.getID());
    }

    private void handle_PlayerSetTrump(Connection connection, Request_PlayerSetTrump object) {
        Request_PlayerSetTrump request =
                object;
        lobby.currentGame.setTrump(request.trump);


        LOG.info("Trump: " + lobby.currentGame.getTrump() + " was set from Client ID: " + connection.getID());
    }

    private void handle_PlayerRequestsCheat(Connection connection, Request_PlayerRequestsCheat object) {
        Request_PlayerRequestsCheat request = object;

        LOG.info("Client " + connection.getID() + " requested cheating");

        lobby.currentGame.givePlayerBestCard(connection.getID());
    }
    private void handle_MixCardsRequest(Connection connection, Request_MixCardsRequest object){
        mixedCards response = new mixedCards();
        lobby.currentGame.mixCards();
        LOG.info("here");
        sendPacketToAll(response);
    }
    /////////////////// END - Handler Methods !!! ///////////////////


    /////////////////// Chat - Handler Methods !!! ///////////////////
    private void handleEndToEndMessage(Request_SendEndToEndChatMessage request) throws Exception {
        final Connection messageReceiverClientConnection = Arrays
                .stream(server.getConnections())
                .filter(connection -> connection.getID() == request.to)
                .findFirst()
                .orElseThrow(() -> new Exception("Client with ID : " + request.to + " not found, so we cannot send the message!"));
        ReceiveEndToEndChatMessage response
                = new ReceiveEndToEndChatMessage(request.message, request.from, request.to);
        messageReceiverClientConnection.sendTCP(response);
    }

    private void handleSendToAllChatMessage(Request_SendToAllChatMessage request) {
        IPackets response =
                new ReceiveToAllChatMessage(request.message, request.from, Utils.getDateAsString());

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

    /////////////////// START - Lobby player List! ///////////////////
    private MutableLiveData<ArrayList<Player>> players = new MutableLiveData<ArrayList<Player>>();

    public LiveData<ArrayList<Player>> getPlayers(){
        return players;
    }

    //////////////////// END - Lobby player List! ////////////////////
}
