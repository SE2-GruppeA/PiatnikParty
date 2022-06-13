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
                e.printStackTrace();
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
                    } else if(object instanceof Requests.PlayerRequestsCheat){
                        handle_PlayerRequestsCheat(connection, (Requests.PlayerRequestsCheat) object);
                    }else if(object instanceof  Requests.MixCardsRequest){
                        handle_MixCardsRequest(connection,(Requests.MixCardsRequest) object);
                    }else if(object instanceof  Requests.ExposePossibleCheater){
                        handle_exposePossibleCheater(connection,(Requests.ExposePossibleCheater) object);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        });
    }

    private void handle_exposePossibleCheater(Connection connection, Requests.ExposePossibleCheater object) {
        String playerId = object.playerId;

        Responses.IsCheater response = new Responses.IsCheater();
        if(isCheater(playerId)){
            response.isCheater = true;
        }else{
            response.isCheater = false;
        }
        connection.sendTCP(response);
    }

    //todo: Add this function to gamelogic itself, it's just here so i can build the handler above !
    private boolean isCheater(String playerId) {
        // todo: Implement gamelogic: how explained down below (Maybe Anton or Bene)!
        // todo: Also add live data !
        /**
         * gameLogic.exposePossibleCheater(playerId)
         *
         * return true if player really cheater with response isCheater(true);
         * also trigger cheater to lose -20 points (he got +10 when cheating, so he is only losing -10)
         *
         * return false, and also trigger a lose -10 points with response isCheater(false)
         * comes with risks !
         */
        return true;
    }

    /////////////////// START - Handler Methods !!! ///////////////////
    private void handle_connected(Connection connection) {
        LOG.info("Client with ID : " + connection.getID() + " just connected");

        Responses.ConnectedSuccessfully response = new Responses.ConnectedSuccessfully();
        response.isConnected = clients.contains(connection) ? false : clients.add(connection);
        response.playerID = connection.getID();

        lobby.addPlayer(connection, "test");
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
        Responses.playerDisconnected response = new Responses.playerDisconnected();
        response.playerID = connection.getID();
        sendPacketToAll(response);

        //players.postValue(wattnGame.getPlayers());
    }

    private void handle_VoteForNextGame(Connection connection, Requests.VoteForNextGame object) {
        LOG.info("Client " + connection.getID() + " voted for" +
                object.gameName.toString());

        lobby.handleVotingForNextGame(connection.getID(), object.gameName);
       // wattnGame.handleVotingForNextGame(connection.getID(),object.gameName);
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
        Responses.GameStartedClientMessage response = new Responses.GameStartedClientMessage();
        sendPacketToAll(response);

        // Message to all Players to open the voting
        handle_ForceVoting(connection);
    }

    private void handle_PlayerSetSchlag(Connection connection, Requests.PlayerSetSchlag object) {
        Requests.PlayerSetSchlag request =
                object;
        lobby.currentGame.setSchlag(request.schlag);
        //wattnGame.deck.setHit(request.schlag);
        LOG.info("Schlag: " + lobby.currentGame.getSchlag() + " was set from Client ID: " + connection.getID());
    }

    private void handle_PlayerSetTrump(Connection connection, Requests.PlayerSetTrump object) {
        Requests.PlayerSetTrump request =
                object;
        lobby.currentGame.setTrump(request.trump);
        //wattnGame.deck.setTrump(request.trump);
        LOG.info("Trump: " + lobby.currentGame.getTrump() + " was set from Client ID: " + connection.getID());
    }

    private void handle_PlayerRequestsCheat(Connection connection, Requests.PlayerRequestsCheat object) {
        Requests.PlayerRequestsCheat request = object;

        LOG.info("Client " + connection.getID() + " requested cheating");

        lobby.currentGame.givePlayerBestCard(connection.getID());
    }
    private void handle_MixCardsRequest(Connection connection,Requests.MixCardsRequest object){
        Requests.MixCardsRequest request = object;
        lobby.currentGame.mixCards();
        LOG.info("here");
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

    /////////////////// START - Lobby player List! ///////////////////
    private MutableLiveData<ArrayList<Player>> players = new MutableLiveData<ArrayList<Player>>();

    public LiveData<ArrayList<Player>> getPlayers(){
        return players;
    }

    //////////////////// END - Lobby player List! ////////////////////
}
