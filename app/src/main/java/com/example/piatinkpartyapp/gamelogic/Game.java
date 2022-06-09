package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.logging.Logger;

public class Game {

    public Lobby lobby;
    public int mainPlayerId;
    public Player roundStartPlayer;

    // Logging for testing
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public Game() {
    }

    public Integer getMainPlayerId() {
        return mainPlayerId;
    }

    public void setMainPlayerId(Integer mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }

    public Player getRoundStartPlayer() {
        return roundStartPlayer;
    }

    public void setRoundStartPlayer(Player roundStartPlayer) {
        this.roundStartPlayer = roundStartPlayer;
    }

    public boolean checkIfAllPlayersFinishedRound() {
        for (Player player : lobby.getPlayers()) {
            if (!player.isRoundFinished()) {
                return false;
            }
        }
        return true;
    }

    public Player getNextPlayer(Player player) {
        int currentIndex = lobby.getPlayers().indexOf(player);
        if (currentIndex == lobby.getPlayers().size() - 1) {
            currentIndex = 0;
            //LOG.info("No next player! return first player");
        } else {
            currentIndex = currentIndex + 1;
            //LOG.info("Index of next player: " + currentIndex);
        }
        return lobby.getPlayers().get(currentIndex);
    }

    // reset points from players
    public void resetPlayerPoints() {
        for (Player player : lobby.getPlayers()) {
            player.resetPoints();
        }
    }

    // reset played card from players
    public void resetPlayedCard() {
        for (Player player : lobby.getPlayers()) {
            player.setCardPlayed(null);
        }
    }

    // reset roundFinished from players
    public void resetRoundFinished() {
        for (Player player : lobby.getPlayers()) {
            player.setRoundFinished(false);
        }
    }

    public void AusgabeTest() {
        LOG.info("ArrayList players: ");
        for (Player player : lobby.getPlayers()) {
            LOG.info("Player: " + player.getId() + " - " + player.getPlayerName());
        }
    }

    // send handcards to players
    public void sendGameStartedMessageToClients() {
        for (Player player : lobby.getPlayers()) {
            // send message to client that game has started
            Responses.SchnopsnStartedClientMessage request = new Responses.SchnopsnStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }

    // notify player: "it's your turn"
    public void notifyPlayerYourTurn(Player player) {
        Responses.NotifyPlayerYourTurn request = new Responses.NotifyPlayerYourTurn();
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    public void sendEndRoundMessageToPlayers(Player roundWinner) {
        for (Player player : lobby.getPlayers()) {
            Responses.EndOfRound response = new Responses.EndOfRound();
            response.playerID = roundWinner.getId();
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        for (Player player : lobby.getPlayers()) {
            Responses.SendPlayedCardToAllPlayers response = new Responses.SendPlayedCardToAllPlayers();
            response.playerID = playerId;
            response.card = card;
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendTrumpToAllPlayers(Symbol symbol) {
        for (Player player : lobby.getPlayers()) {
            Responses.SendTrumpToAllPlayers response = new Responses.SendTrumpToAllPlayers();
            response.trump = symbol;
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendPointsToWinnerPlayer(Player winnerPlayer) {
        Responses.UpdatePointsWinnerPlayer response = new Responses.UpdatePointsWinnerPlayer();
        response.totalPoints = winnerPlayer.getPoints();
        winnerPlayer.getClientConnection().sendTCP(response);
    }

    public void startGameSchnopsn() {
    }
    public void startGameWattn(){

    }

    public void startGameHosnObe() {

    }

    public void setCard(int id, Card card) {
    }

    public void givePlayerBestCard(int playerId){
    }
    public void setSchlag(CardValue hit){

    }
    public void setTrump(Symbol trump){

    }
    public CardValue getSchlag(){
        return null;
    }
    public Symbol getTrump(){
        return null;
    }
}
