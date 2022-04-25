package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Packets;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Game {
    private int mainPlayerId;
    private Player currentPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private SchnopsnDeck deck;

    // Logging for testing
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public Game() {
    }

    public Integer getMainPlayerId() {
        return mainPlayerId;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setMainPlayerId(Integer mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void resetSchnopsnDeck() {
        deck = new SchnopsnDeck(GameName.Schnopsn, 2);
    }

    public Player addPlayer(Connection connection, String playerName) {
        Player player = new Player(connection, playerName);
        players.add(player);
        return player;
    }

    // reset points from players
    public void resetPlayerPoints() {
        for (Player player: players) {
            player.resetPoints();
        }
    }

    // reset roundFinished from players
    public void resetRoundFinished() {
        for (Player player: players) {
            player.setRoundFinished(false);
        }
    }

    // start the game
    public void startGame() {
        new Thread(()->{
            AusgabeTest();
            resetRoundFinished();
            sendHandCards();
            // notify first player that it is his turn
            notifyPlayerYourTurn(players.get(0));
        }).start();
    }

    public void AusgabeTest() {
        LOG.info("ArrayList players: ");
        for (Player player : players) {
            LOG.info("Player: " + player.getId() + " - " + player.getPlayerName());
        }
    }

    // send handcards to players
    public void sendHandCards() {
        for (Player player: players) {
            Packets.Responses.SendHandCards request = new Packets.Responses.SendHandCards();
            request.cards = deck.getHandCards();
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
        }
    }

    // notify player: "it's your turn"
    public void notifyPlayerYourTurn(Player player) {
        Packets.Responses.NotifyPlayerYourTurn request = new Packets.Responses.NotifyPlayerYourTurn();
        request.playerID =  player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }
}
