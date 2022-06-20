package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.Responses.Response_CheatingPenalty;
import com.example.piatinkpartyapp.networking.Responses.Response_EndOfRound;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.Responses.Response_SchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_SendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.*;
import java.util.TreeMap;
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

    public void resetCheating(){
        for (Player player : lobby.getPlayers()) {
            player.setHasExposed(false);
            player.setCheaten(false);
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
            Response_SchnopsnStartedClientMessage request = new Response_SchnopsnStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }

    // notify player: "it's your turn"
    public void notifyPlayerYourTurn(Player player) {
        Response_NotifyPlayerYourTurn request = new Response_NotifyPlayerYourTurn();
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    public void sendEndRoundMessageToPlayers(Player roundWinner) {
        for (Player player : lobby.getPlayers()) {
            Response_EndOfRound response = new Response_EndOfRound();
            response.playerID = roundWinner.getId();
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        for (Player player : lobby.getPlayers()) {
            Response_SendPlayedCardToAllPlayers response = new Response_SendPlayedCardToAllPlayers();
            response.playerID = playerId;
            response.card = card;
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendTrumpToAllPlayers(Symbol symbol) {
        for (Player player : lobby.getPlayers()) {
            Response_SendTrumpToAllPlayers response = new Response_SendTrumpToAllPlayers();
            response.trump = symbol;
            player.getClientConnection().sendTCP(response);
        }

    }

    public void sendSchlagToAllPlayers(CardValue cardValue){
        for(Player player: lobby.getPlayers()){
            Response_SendSchlagToAllPlayers response = new Response_SendSchlagToAllPlayers(cardValue);
            //response.schlag = cardValue;
            player.getClientConnection().sendTCP(response);
        }
    }
    public void sendPointsToWinnerPlayer(Player winnerPlayer) {
        Response_UpdatePointsWinnerPlayer response = new Response_UpdatePointsWinnerPlayer();
        response.totalPoints = winnerPlayer.getPoints();
        winnerPlayer.getClientConnection().sendTCP(response);
    }

    public void sendRoundWinnerPlayerToAllPlayers(Player winnerPlayer) {
        Response_SendRoundWinnerPlayerToAllPlayers response = new Response_SendRoundWinnerPlayerToAllPlayers();
        response.winnerPlayerID = winnerPlayer.getId();

        for (Player player : lobby.getPlayers()) {
            player.getClientConnection().sendTCP(response);
        }
    }

    public void addPointsAndUpdateScoreboard(Player winnerPlayer, int pointScoreboardAdd) {
        lobby.getPlayerByID(winnerPlayer.getId()).addPointsScoreboard(pointScoreboardAdd);

        sendMessageUpdateScoreboard();
    }

    public void sendMessageUpdateScoreboard() {
        Response_UpdateScoreboard response = new Response_UpdateScoreboard(getPlayerHashMap());

        for (Player player : lobby.getPlayers()) {
            player.getClientConnection().sendTCP(response);
        }
    }

    public TreeMap<String, Integer> getPlayerHashMap(){
        TreeMap<String, Integer> playersHashMap = new TreeMap<>();

        for(Player player: lobby.getPlayers()){
            playersHashMap.put(player.getPlayerName(), player.getPointsScoreboard());
        }

        return playersHashMap;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    /*
    // DOnt know why this methode is empty.
     */
    public void startGame() {

    }

    /*
    // DOnt know why this methode is empty.
    */
    public void setCard(int id, Card card) {

    }

    public void givePlayerBestCard(int playerId){
        lobby.getPlayerByID(playerId).setCheaten(true);
    }

    /*
    // DOnt know why this methode is empty.
    */
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

    /*
    // DOnt know why this methode is empty.
    */
    public void mixCards() {

    }

    public Boolean isPlayerCheater(Integer playerId, Integer exposerId) {
        lobby.getPlayerByID(exposerId).setHasExposed(true);
        return lobby.getPlayerByID(playerId).isCheaten();
    }

    public void cheaterPenalty(Integer playerId) {
        Player player = lobby.getPlayerByID(playerId);

        if(player.isCheaten()){
            player.addPoints(-20);
        }

        sendPenaltyMessageToPlayer(player);
    }

    public void sendPenaltyMessageToPlayer(Player player) {
        Response_CheatingPenalty response = new Response_CheatingPenalty();
        player.getClientConnection().sendTCP(response);
        sendPointsToWinnerPlayer(player);
    }

    public void punishWrongExposure(Integer exposerId) {
        Player player = lobby.getPlayerByID(exposerId);
        player.addPoints(-10);
        sendPointsToWinnerPlayer(player);
    }
}
