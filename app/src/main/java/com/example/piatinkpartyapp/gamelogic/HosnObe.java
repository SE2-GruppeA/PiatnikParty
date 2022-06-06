package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.ArrayList;
import java.util.logging.Logger;

public class HosnObe extends Game {

    private Player roundStartPlayer;
    public ArrayList<Integer> playerPoints = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();
    public WattnDeck deck;

    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public HosnObe() {
        resetHosnObeDeck();
    }

    @Override
    public Player getRoundStartPlayer() {
        return roundStartPlayer;
    }

    @Override
    public GameName getWinnerGameOfVoting() {
        return super.getWinnerGameOfVoting();
    }

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    public Integer getMainPlayerId() {
        return super.getMainPlayerId();
    }

    @Override
    public Player getPlayerByID(int playerID) {
        return super.getPlayerByID(playerID);
    }

    @Override
    public Player getRoundWinnerPlayerSchnopsn() {
        return super.getRoundWinnerPlayerSchnopsn();
    }

    public ArrayList<Integer> getPlayerPoints() {
        return playerPoints;
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public WattnDeck getDeck() {
        return deck;
    }

    @Override
    public void sendGameStartedMessageToClients() {
        super.sendGameStartedMessageToClients();
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setDeck(WattnDeck deck) {
        this.deck = deck;
    }

    @Override
    public void setMainPlayerId(Integer mainPlayerId) {
        super.setMainPlayerId(mainPlayerId);
    }

    @Override
    public void sendHandCards() {
        super.sendHandCards();
    }

    @Override
    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        super.sendPlayedCardToAllPlayers(playerId, card);
    }

    @Override
    public void sendPointsToWinnerPlayer(Player winnerPlayer) {
        super.sendPointsToWinnerPlayer(winnerPlayer);
    }

    public void setPlayerPoints(ArrayList<Integer> playerPoints) {
        this.playerPoints = playerPoints;
    }

    @Override
    public void setRoundStartPlayer(Player roundStartPlayer) {
        this.roundStartPlayer = roundStartPlayer;
    }

    @Override
    public Player addPlayer(Connection connection, String playerName) {
        Player player = new Player(connection, playerName);
        players.add(player);
        return player;
    }

    @Override
    public void addPointsToWinnerPlayer(Player winnerPlayer) {
        super.addPointsToWinnerPlayer(winnerPlayer);
    }

    public void resetVotingFinished() {
        for (Player player : players) {
            player.setVotingFinished(false);
        }
    }

    @Override
    public void resetSchnopsnDeck() {
        super.resetSchnopsnDeck();
    }

    @Override
    public void resetPlayedCard() {
        super.resetPlayedCard();
    }

    @Override
    public void resetPlayerPoints() {
        super.resetPlayerPoints();
    }

    @Override
    public void resetRoundFinished() {
        super.resetRoundFinished();
    }

    @Override
    public boolean checkIfAllPlayersFinishedVoting() {
        return super.checkIfAllPlayersFinishedVoting();
    }

    public Player getRoundWinnerHosnObe() {

        Player roundWinner = players.get(0);
        Player currentPlayer = getNextPlayer(roundWinner);

        return roundWinner;
    }

    public void setHandCards() {
        for (Player player : players) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);
            Responses.SendHandCards request = new Responses.SendHandCards();
            request.cards = handCards;
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
        }
    }

    @Override
    public void setCard(int playerID, Card card) {

        Player player = getPlayerByID(playerID);

        new Thread(() -> {
            player.setRoundFinished(true);
            player.setCardPlayed(card);
            sendPlayedCardToAllPlayers(playerID, card);
            player.removeHandcard(card);

            if (checkIfAllPlayersFinishedRound()) {
                Player roundWonPlayer = getRoundWinnerPlayerSchnopsn();
                addPointsToWinnerPlayer(roundWonPlayer);
                startNewRoundSchnopsn(roundWonPlayer);
            } else {
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

    @Override
    public Player getNextPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        if (currentIndex == players.size()-1) {
            currentIndex = 0;
        } else {
            currentIndex = currentIndex + 1;
        }
        return players.get(currentIndex);
    }

    @Override
    public boolean checkIfAllPlayersFinishedRound() {
        for (Player player: players) {
            if (!player.isRoundFinished()) {
                return false;
            }
        }
        return true;
    }

    public void startNewRoundHosnObe(Player startplayer) {
        new Thread(()->{
            if (startplayer.getPoints() >= 3 ) {
                sendEndRoundMessageToPlayers(startplayer);
            } else {
                resetRoundFinished();
                resetPlayedCard();
                setRoundStartPlayer(startplayer);
                notifyPlayerYourTurn(startplayer);
            }
        }).start();
    }

    @Override
    public void sendEndRoundMessageToPlayers(Player roundWinner) {
        super.sendEndRoundMessageToPlayers(roundWinner);
    }

    @Override
    public void sendTrumpToAllPlayers(Symbol symbol) {
        super.sendTrumpToAllPlayers(symbol);
    }

    public void resetHosnObeDeck() {
        int numberOfPlayers = players.size();
        deck = new WattnDeck(GameName.HosnObe,numberOfPlayers);
        for (int i = 0; i <= numberOfPlayers; i++) {
            playerPoints.add(i);
        }
    }

    public void startNewRoundHosnObe(Player startPlayer) {

        Thread thread;
        thread = new Thread(() -> {
            if (startPlayer.getPoints() >= 3 ) {
                sendEndRoundMessageToPlayers(startPlayer);
            } else {
                resetRoundFinished();
            }
        }).start();
    }
}
