package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.PensionistlnRound;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.responses.responsePensionistLnStartedClientMessage;

import java.util.ArrayList;

public class PensionistlnGame extends SchnopsnGame{

    public PensionistlnGame(Lobby l) {
        super(l);
    }
    private PensionistlnRound currentRound;


    @Override
    public void startGame() {
        new Thread(() -> {
            AusgabeTest();
            resetSchnopsnDeck();
            sendMessageUpdateScoreboard();
            setCurrentRound();
            sendGameStartedMessageToClients();
            resetCheating();
            resetRoundFinished();
            resetPlayerPoints();
            sendHandCards();

            sendTrumpToAllPlayers(getDeck().getTrump());

            Player roundStartPlayer = getRandomPlayer();
            setRoundStartPlayer(roundStartPlayer);
            // notify first player that it is his turn
            notifyPlayerYourTurn(roundStartPlayer);
        }).start();
    }

    private void setCurrentRound() {
        currentRound = PensionistlnRound.getRandomRound();
    }

    @Override
    public void sendGameStartedMessageToClients() {
        for (Player player : lobby.getPlayers()) {
            // send message to client that game has started
            responsePensionistLnStartedClientMessage request = new responsePensionistLnStartedClientMessage(currentRound);
            player.getClientConnection().sendTCP(request);
        }
    }

    @Override
    public void givePlayerBestCard(int playerId) {
        lobby.getPlayerByID(playerId).setCheaten(true);
        sendPlayerBestCard(playerId, new Card(Symbol.randomSymbol(), CardValue.SIEBEN));
    }

    @Override
    public void setCard(int playerID, Card card) {
        new Thread(() -> {
            Player player = lobby.getPlayerByID(playerID);

            //set and remove played card from players handcards
            player.setCardPlayed(card);
            player.removeHandcard(card);

            //broadcast played card
            sendPlayedCardToAllPlayers(playerID, card);

            player.setRoundFinished(true);

            if (checkIfAllPlayersFinishedRound()) {
                ArrayList<Player> losingPlayers = getRoundLosingPlayers();

                sendPointsToLosingPlayers(losingPlayers);
                sendRoundLosingPlayersToAllPlayers(losingPlayers);

                startNewRoundPensionistln(getWinningPlayer());
            } else {
                // Nächsten Spieler benachrichtigen dass er dran ist
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

    private Player getWinningPlayer() {
        Player winner = getRoundStartPlayer();

        for(Player player1:lobby.getPlayers()){
            for(Player player2:lobby.getPlayers()){
                if(player1!=player2 && player1.getPoints()>player2.getPoints()){
                    winner = player1;
                }
            }
        }

        return winner;
    }

    private void sendRoundLosingPlayersToAllPlayers(ArrayList<Player> losingPlayers) {
        for(Player player:losingPlayers) {
            sendRoundWinnerPlayerToAllPlayers(player);
        }
    }

    public void startNewRoundPensionistln(Player startPlayer){
        new Thread(() -> {
            if (startPlayer.getHandcards().isEmpty()) {
                addPointsAndUpdateScoreboard(startPlayer,1);
                ArrayList<Player> winner = new ArrayList<>();
                winner.add(startPlayer);

                sendEndRoundMessageToPlayers(winner);
            } else {
                resetRoundFinished();
                resetPlayedCard();
                handoutCard();
                setRoundStartPlayer(startPlayer);
                notifyPlayerYourTurn(startPlayer);
            }
        }).start();
    }

    public ArrayList<Player> getRoundLosingPlayers(){
        CardValue highestCardValue = getHighestPlayedValue();
        ArrayList<Player> losingPlayers = new ArrayList<>();

        for(Player p: lobby.getPlayers()){
            if(p.getCardPlayed().getCardValue() == highestCardValue){
                losingPlayers.add(p);
            }
        }

        return losingPlayers;
    }

    public CardValue getHighestPlayedValue(){
        CardValue highestValue = CardValue.SIEBEN;

        for(Player player: lobby.getPlayers()){
            if(getDeck().cardPoints(player.getCardPlayed().getCardValue()) >
                    getDeck().cardPoints(highestValue)){
                highestValue = player.getCardPlayed().getCardValue();
            }
        }

        return highestValue;
    }

    public void sendPointsToLosingPlayers(ArrayList<Player> losingPlayers) {
        for(Player player : losingPlayers) {
            player.addPoints(-1);
            sendPointsToWinnerPlayer(player);
        }
    }
}
