package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.GameServer;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Player {
    private Integer id;
    private String playerName;
    private int points = 0;
    private int pointsScoreboard = 0;
    private ArrayList<Card> handcards;
    private ArrayList<Card> cardsWon;
    private Card cardPlayed;
    private boolean finished;
    private boolean roundFinished;
    private boolean cheaten;
    private Connection clientConnection;
    private boolean votingFinished;
    private GameName votingGame;

    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public Player() {
    }

    public ArrayList<Card> getCardsWon() {
        return cardsWon;
    }

    public static Logger getLOG() {
        return LOG;
    }

    public Player(Connection connection, String playerName) {
        this.id = connection.getID();
        this.playerName = playerName;
        this.clientConnection = connection;
    }

    public Card addHandcard(Card card) {
        handcards.add(card);
        return card;
    }

    public void removeHandcard(Card card) {
        LOG.info(card.getSymbol().toString());
        LOG.info(card.getCardValue().toString());
        Log.error(this.handcards.toString());
        LOG.info("remove card: " +  card.getSymbol().toString() + card.getCardValue().toString());
        if (handcards.contains(card)) {
            handcards.remove(card);
        } else {
            LOG.info("card could not removed because it's not part of handcards");
        }
    }

    public Card addCardsWon(Card card) {
        cardsWon.add(card);
        return card;
    }

    public void clearCardsWon() {
        cardsWon.clear();
    }

    public void addPoints(int pointsWon) {
        points = points + pointsWon;
    }

    public void resetPoints() {
        points = 0;
    }

    public void addPointsScoreboard(int pointsWon) {
        pointsScoreboard = pointsScoreboard + pointsWon;
    }

    public void resetPointsScoreboard() {
        pointsScoreboard = 0;
    }

    // Generated functions
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Card> getHandcards() {
        return handcards;
    }

    public void setHandcards(ArrayList<Card> handcards) {
        this.handcards = handcards;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setRoundFinished(boolean roundFinished) {
        this.roundFinished = roundFinished;
    }

    public void setCheaten(boolean cheaten) {
        this.cheaten = cheaten;
    }

    public void setClientConnection(Connection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public Integer getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    public int getPointsScoreboard() {
        return pointsScoreboard;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }

    public boolean isCheaten() {
        return cheaten;
    }

    public void setCheaten(Boolean cheat) {this.cheaten = cheat; }

    public Connection getClientConnection() {
        return clientConnection;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public boolean isVotingFinished() {
        return votingFinished;
    }

    public void setVotingFinished(boolean votingFinished) {
        this.votingFinished = votingFinished;
    }

    public GameName getVotingGame() {
        return votingGame;
    }

    public void setVotingGame(GameName votingGame) {
        this.votingGame = votingGame;
    }
}
