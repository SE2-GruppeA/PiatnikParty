package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.networking.GameServer;

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
        }).start();
    }

    public void AusgabeTest() {
        LOG.info("ArrayList players: ");
        for (Player player : players) {
            LOG.info("Player: " + player.getId() + " - " + player.getPlayerName());
        }
    }
}
