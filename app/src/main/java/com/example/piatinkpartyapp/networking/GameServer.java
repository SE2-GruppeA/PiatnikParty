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
import com.example.piatinkpartyapp.networking.requests.requestExposePossibleCheater;
import com.example.piatinkpartyapp.networking.requests.requestForceVoting;
import com.example.piatinkpartyapp.networking.requests.requestMixCardsRequest;
import com.example.piatinkpartyapp.networking.requests.requestPlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetCard;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetSchlag;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetTrump;
import com.example.piatinkpartyapp.networking.requests.requestSendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestSendToAllChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestStartGameMessage;
import com.example.piatinkpartyapp.networking.requests.requestVoteForNextGame;
import com.example.piatinkpartyapp.networking.responses.responseConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.responses.responseEndOfGame;
import com.example.piatinkpartyapp.networking.responses.responseGameStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseIsCheater;
import com.example.piatinkpartyapp.networking.responses.responseMixedCards;
import com.example.piatinkpartyapp.networking.responses.responsePlayerDisconnected;
import com.example.piatinkpartyapp.networking.responses.responseReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseServerMessage;
import com.example.piatinkpartyapp.networking.responses.responseUpdateScoreboard;
import com.example.piatinkpartyapp.networking.responses.responseVoteForNextGame;
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
    private ArrayList<Connection> clients;
    private Lobby lobby;
    private Game game;
    private WattnGame wattnGame;
    private ExecutorService executorService;

    private static GameServer INSTANCE = null;

    public static GameServer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GameServer();
        }
        return INSTANCE;
    }

    public void startNewGameServer() throws IOException {
        executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            server = new Server();
            clients = new ArrayList<>();
            NetworkHandler.register(server.getKryo());
            // this line of code has to run before we start / bind / connect to the server !!
            server.start();
            try {
                server.bind(NetworkHandler.TCP_Port, NetworkHandler.TCP_UDP);
            } catch (IOException e) {
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
                    handleConnected(connection);
                    super.connected(connection);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                handleDisconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (object instanceof requestSendEndToEndChatMessage) {
                        handleEndToEndMessage((requestSendEndToEndChatMessage) object);
                    } else if (object instanceof requestSendToAllChatMessage) {
                        handleSendToAllChatMessage((requestSendToAllChatMessage) object);
                    } else if (object instanceof requestStartGameMessage) {
                        handleStartGameMessage(connection);
                    } else if (object instanceof requestPlayerSetCard) {
                        handlePlayerSetCard(connection, (requestPlayerSetCard) object);
                    } else if (object instanceof requestForceVoting){
                        handleForceVoting(connection);
                    } else if(object instanceof requestVoteForNextGame){
                        handleVoteForNextGame(connection, (requestVoteForNextGame) object);
                    } else if (object instanceof requestPlayerSetSchlag) {
                        handlePlayersetschlag(connection, (requestPlayerSetSchlag) object);
                    } else if (object instanceof requestPlayerSetTrump) {
                        handlePlayerSetTrump(connection, (requestPlayerSetTrump) object);
                    } else if(object instanceof requestPlayerRequestsCheat){
                        handlePlayerRequestsCheat(connection, (requestPlayerRequestsCheat) object);
                    }else if(object instanceof requestMixCardsRequest){
                        handleMixCardsRequest();
                    }else if(object instanceof requestExposePossibleCheater){
                        handleExposePossibleCheater(connection,(requestExposePossibleCheater) object);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }


    public void closeGame(){
        responseEndOfGame response = new responseEndOfGame();

        LOG.info(" Server requested to close the game");

        sendPacketToAll(response);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.interrupted();
            e.printStackTrace();
        }

        for(Connection c:clients){
            c.close();
        }

        server.close();
        executorService.shutdown();

        LOG.info("Server closed");
    }

    private void handleExposePossibleCheater(Connection connection, requestExposePossibleCheater object) {
        Integer playerId = object.playerId;
        String Client = "Client";

        responseIsCheater response = new responseIsCheater();

        LOG.info(Client + connection.getID() + " wants to expose client " + object.playerId);

        //player can only expose one time a round and cannot expose himself
        if(playerId != connection.getID()) {
            if(!lobby.getPlayerByID(connection.getID()).hasExposedPlayer()){
                if (isCheater(playerId, connection.getID())) {
                    response.isCheater = true;
                } else {
                    response.isCheater = false;
                }

                LOG.info(Client  + object.playerId + " cheating: " + response.isCheater);
                connection.sendTCP(response);
            }else{
                LOG.info(Client  + connection.getID() + " already cheated");
                sendPacket(connection, new responseServerMessage("Du kannst pro Runde nur einmal exposen!"));
            }
        }else{
            LOG.info(Client  + connection.getID() + " tried to expose himself");
            sendPacket(connection, new responseServerMessage("Du kannst dich nicht selbst exposen!"));
        }
    }

    private boolean isCheater(Integer playerId, Integer exposerId) {

        Boolean isCheater = lobby.currentGame.isPlayerCheater(playerId, exposerId);

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
    private void handleConnected(Connection connection) {
        LOG.info("Client with ID : " + connection.getID() + " just connected");

        responseConnectedSuccessfully response = new responseConnectedSuccessfully();
        response.isConnected = !clients.contains(connection) && clients.add(connection);
        response.playerID = connection.getID();

        lobby.addPlayer(connection, "Player " + connection.getID());
        connection.sendTCP(response);

        //update teilnehmerliste (in clients stehen alle verbundenen clients)
        players.postValue(lobby.getPlayers());

        sendPacketToAll(new responseUpdateScoreboard(lobby.getPlayerHashMap()));
        //players.postValue(wattnGame.getPlayers());
    }

    private void handleDisconnected(Connection connection) {
        //update teilnehmerliste (in clients stehen alle verbundenen clients)
        Player player = lobby.getPlayerByID(connection.getID());
        clients.remove(player);
        lobby.removePlayer(player);
        players.postValue(lobby.getPlayers());

        //When the Player disconnects the message is send to all other players.
        responsePlayerDisconnected response = new responsePlayerDisconnected();
        response.playerID = connection.getID();
        sendPacketToAll(response);
    }

    private void handleVoteForNextGame(Connection connection, requestVoteForNextGame object) {
        LOG.info("Client " + connection.getID() + " voted for" +
                object.gameName.toString());

        lobby.handleVotingForNextGame(connection.getID(), object.gameName);

    }

    private void handleForceVoting(Connection connection) {
        LOG.info("Voting has been initiated by client " + connection.getID());

        responseVoteForNextGame response =
                new responseVoteForNextGame();

        sendPacketToAll(response);

        LOG.info("VoteForNextGame sent to all Clients");
    }

    private void handlePlayerSetCard(Connection connection, requestPlayerSetCard object) {
        requestPlayerSetCard request =
                object;

        LOG.info("Card: " + request.card.getSymbol().toString() + request.card.getCardValue().toString() + " was set from Client ID: " + connection.getID());
        lobby.currentGame.setCard(connection.getID(), request.card);

    }

    private void handleStartGameMessage(Connection connection) {

        LOG.info("Game started on server : " + NetworkHandler.GAMESERVER_IP +
                ", Client ID started the game: " + connection.getID());

        // Message to all Players that game has started to open the gamefragment in order to open voting
        responseGameStartedClientMessage response = new responseGameStartedClientMessage();
        sendPacketToAll(response);

        // Message to all Players to open the voting
        handleForceVoting(connection);
    }

    private void handlePlayersetschlag(Connection connection, requestPlayerSetSchlag object) {
        requestPlayerSetSchlag request =
                object;
        lobby.currentGame.setSchlag(request.schlag);

        LOG.info("Schlag: " + lobby.currentGame.getSchlag() + " was set from Client ID: " + connection.getID());
    }

    private void handlePlayerSetTrump(Connection connection, requestPlayerSetTrump object) {
        requestPlayerSetTrump request =
                object;
        lobby.currentGame.setTrump(request.trump);


        LOG.info("Trump: " + lobby.currentGame.getTrump() + " was set from Client ID: " + connection.getID());
    }

    private void handlePlayerRequestsCheat(Connection connection, requestPlayerRequestsCheat object) {
        requestPlayerRequestsCheat request = object;
        LOG.info("Client " + connection.getID() + " requested cheating");

        Player player = lobby.getPlayerByID(connection.getID());

        if(!player.isCheaten()){
            lobby.currentGame.givePlayerBestCard(connection.getID());
            LOG.info("Cheating successfull for client " + connection.getID());
        }else{
            sendPacket(connection, new responseServerMessage("Du kannst nur einmal pro Runde cheaten"));
            LOG.info("Error: Client " + connection.getID() + " already cheated this round");
        }

    }
    private void handleMixCardsRequest(){
        responseMixedCards response = new responseMixedCards();
        lobby.currentGame.mixCards();
        LOG.info("here");
        sendPacketToAll(response);
    }
    /////////////////// END - Handler Methods !!! ///////////////////


    /////////////////// Chat - Handler Methods !!! ///////////////////
    private void handleEndToEndMessage(requestSendEndToEndChatMessage request) throws Exception {
        final Connection messageReceiverClientConnection = Arrays
                .stream(server.getConnections())
                .filter(connection -> connection.getID() == request.to)
                .findFirst()
                .orElseThrow(() -> new Exception("Client with ID : " + request.to + " not found, so we cannot send the message!"));
        responseReceiveEndToEndChatMessage response
                = new responseReceiveEndToEndChatMessage(request.message, request.from, request.to);
        messageReceiverClientConnection.sendTCP(response);
    }

    private void handleSendToAllChatMessage(requestSendToAllChatMessage request) {
        IPackets response =
                new responseReceiveToAllChatMessage(request.message, request.from, Utils.getDateAsString());

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
