package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;

public class HosnObeGame extends Game {

    public HosnObeGame(Lobby l) {

        lobby = l;
    }


    @Override
    public CardValue getSchlag() {
        return super.getSchlag();
    }

    @Override
    public Integer getMainPlayerId() {
        return super.getMainPlayerId();
    }

    @Override
    public Player getNextPlayer(Player player) {
        return super.getNextPlayer(player);
    }

    @Override
    public Player getRoundStartPlayer() {
        return super.getRoundStartPlayer();
    }

    @Override
    public Symbol getTrump() {
        return super.getTrump();
    }

    @Override
    public void sendEndRoundMessageToPlayers(Player roundWinner) {
        super.sendEndRoundMessageToPlayers(roundWinner);
    }

    @Override
    public void sendGameStartedMessageToClients() {
        super.sendGameStartedMessageToClients();
    }

    @Override
    public void setCard(int id, Card card) {
        super.setCard(id, card);
    }

    @Override
    public void setMainPlayerId(Integer mainPlayerId) {
        super.setMainPlayerId(mainPlayerId);
    }

    @Override
    public void setRoundStartPlayer(Player roundStartPlayer) {
        super.setRoundStartPlayer(roundStartPlayer);
    }

    @Override
    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        super.sendPlayedCardToAllPlayers(playerId, card);
    }

    @Override
    public void sendPointsToWinnerPlayer(Player winnerPlayer) {
        super.sendPointsToWinnerPlayer(winnerPlayer);
    }

    @Override
    public void setSchlag(CardValue hit) {
        super.setSchlag(hit);
    }

    @Override
    public void setTrump(Symbol trump) {
        super.setTrump(trump);
    }

    @Override
    public void sendTrumpToAllPlayers(Symbol symbol) {
        super.sendTrumpToAllPlayers(symbol);
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
}
