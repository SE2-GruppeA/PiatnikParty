package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.HosnObeDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.ArrayList;

public class HosnObeGame extends Game {

    public HosnObeDeck hosnObeDeck;

    public HosnObeGame(Lobby l) {
        resetHosnObeDeck(l.getPlayers().size());
        lobby = l;
    }

    public void startNewRoundHosnObe(Player startPlayer) {

        new Thread(()->{
            if (startPlayer.getPoints() >= 3 ) {
                sendEndRoundMessageToPlayers(startPlayer);

            }
            else if(startPlayer.getHandcards().isEmpty()) {
                sendEndRoundMessageToPlayers(startPlayer);
            }
            else {
                resetRoundFinished();
                resetPlayedCard();

                setRoundStartPlayer(startPlayer);
                notifyPlayerYourTurn(startPlayer);
            }
        }).start();
    }

    public void addPointsToWinnerPlayer(Player winnerPlayer) {

        if (lobby.getPlayers().size() == 2) {

        } else if (lobby.getPlayers().size() == 3) {

        } else if (lobby.getPlayers().size() == 4) {

        } else {
            winnerPlayer.addPoints(1);
            sendPointsToWinnerPlayer(winnerPlayer);
        }

        /*
        if (lobby.getPlayers().size() == 3 && (winnerPlayer.getId() == 2 || winnerPlayer.getId() == 3)) {
            lobby.getPlayerByID(2).addPoints(1);
            sendPointsToWinnerPlayer(lobby.getPlayerByID(2));
            lobby.getPlayerByID(3).addPoints(1);
            sendPointsToWinnerPlayer(lobby.getPlayerByID(3));
        }
        else if (lobby.getPlayers().size() == 4) {
            if (winnerPlayer.getId() == 1 || winnerPlayer.getId() == 3) {
                lobby.getPlayerByID(1).addPoints(1);
                sendPointsToWinnerPlayer(lobby.getPlayerByID(1));
                lobby.getPlayerByID(3).addPoints(1);
                sendPointsToWinnerPlayer(lobby.getPlayerByID(3));
            }
            else if (winnerPlayer.getId() == 2 || winnerPlayer.getId() == 4) {
                lobby.getPlayerByID(2).addPoints(1);
                sendPointsToWinnerPlayer(lobby.getPlayerByID(2));
                lobby.getPlayerByID(4).addPoints(1);
                sendPointsToWinnerPlayer(lobby.getPlayerByID(4));
            }
        }
        else {
            winnerPlayer.addPoints(1);
            sendPointsToWinnerPlayer(winnerPlayer);
        }
        */
    }

    public Player getRoundWinnerHosnObe() {

        Player winningPlayer = this.roundStartPlayer;
        Player currentPlayer = getNextPlayer(this.roundStartPlayer);

        while (currentPlayer != this.roundStartPlayer) {

            if (winningPlayer.getCardPlayed().getCardValue() == this.hosnObeDeck.getHighestCard().getCardValue()
                    && winningPlayer.getCardPlayed().getSymbol() == hosnObeDeck.getHighestCard().getSymbol()) {
                winningPlayer = winningPlayer;
            }
            //second played card is right card
            else if (currentPlayer.getCardPlayed().getSymbol() == this.hosnObeDeck.getHighestCard().getSymbol()
                    && currentPlayer.getCardPlayed().getCardValue() == hosnObeDeck.getHighestCard().getCardValue()) {
                winningPlayer = currentPlayer;

            } //hit case - first played hit wins
            else if (winningPlayer.getCardPlayed().getCardValue() == this.hosnObeDeck.getHighestCard().getCardValue() ){
                winningPlayer = winningPlayer;
            }//hit case - hit wins
            else if (currentPlayer.getCardPlayed().getCardValue() ==this.hosnObeDeck.getHit()
                    && winningPlayer.getCardPlayed() != this.hosnObeDeck.getHighestCard()) {

                winningPlayer = currentPlayer;
            } // trump case - higher trump wins
            else if (winningPlayer.getCardPlayed().getSymbol() == this.hosnObeDeck.getTrump()){
                if(currentPlayer.getCardPlayed().getSymbol() == this.hosnObeDeck.getTrump()
                        && this.hosnObeDeck.cardPoints(currentPlayer.getCardPlayed().cardValue) >
                        this.hosnObeDeck.cardPoints(winningPlayer.getCardPlayed().cardValue)) {
                    winningPlayer = currentPlayer;
                }
                //default cases: if same colour higher value wins, if trump & other value trump wins
            }
            else if(currentPlayer.getCardPlayed().getSymbol() == this.hosnObeDeck.getTrump()
                    || (winningPlayer.getCardPlayed().getSymbol() == currentPlayer.getCardPlayed().getSymbol()
                    && this.hosnObeDeck.cardPoints(currentPlayer.getCardPlayed().cardValue) >
                    this.hosnObeDeck.cardPoints(winningPlayer.getCardPlayed().cardValue))) {
                winningPlayer = currentPlayer;
            }
            /*Integer p = playerPoints.get(winningPlayer.getId()-1);
            p++;
            playerPoints.set(winningPlayer.getId()-1,p);*/
            if (winningPlayer.getPoints() == 3){
                sendEndRoundMessageToPlayers(roundStartPlayer);
                return winningPlayer;
            }
            currentPlayer = getNextPlayer(currentPlayer);

        }

        return winningPlayer;
    }

    @Override
    public void setCard(int playerID, Card card) {

        Player player = lobby.getPlayerByID(playerID);

        new Thread(()->{
            player.setRoundFinished(true);
            player.setCardPlayed(card);
            sendPlayedCardToAllPlayers(playerID, card);
            player.removeHandcard(card);
            if (checkIfAllPlayersFinishedRound()) {
                Player roundWonPlayer = getRoundWinnerHosnObe();
                addPointsToWinnerPlayer(roundWonPlayer);
                startGameHosnObe();
            }
            else {
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

    public void sendPlayerBestCard(int playerId, Card card){
        Player player = lobby.getPlayerByID(playerId);
        ArrayList<Card> currentHandCards = player.getHandcards();

        currentHandCards.set(0, card);
        player.setHandcards(currentHandCards);

        sendHandCardsToPlayer(currentHandCards, player);
    }

    @Override
    public void givePlayerBestCard(int playerId) {
        sendPlayerBestCard(playerId, new Card(this.hosnObeDeck.getTrump(), this.hosnObeDeck.getHit()));
    }

    private void sendHandCardsToPlayer(ArrayList<Card> handCards, Player player) {
        Responses.SendHandCards request = new Responses.SendHandCards();
        request.cards = handCards;
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    private void sendHandCards() {

        for (Player player: lobby.getPlayers()) {

            ArrayList<Card> handCards = hosnObeDeck.getHandCards();
            player.setHandcards(handCards);

            sendHandCardsToPlayer(handCards, player);

            if (player.getId() == 1) {
                player.getClientConnection()
                        .sendTCP(new Responses.NotifyPlayerToSetSchlag());
            } else if (player.getId() == 2) {
                player.getClientConnection()
                        .sendTCP(new Responses.NotifyPlayerToSetTrump());
            }
        }
    }

    @Override
    public void startGameHosnObe() {

        new Thread(()->{
            resetHosnObeDeck(lobby.getPlayers().size());
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
            setRoundStartPlayer(lobby.getPlayers().get(0));
            notifyPlayerYourTurn(lobby.getPlayers().get(0));
        }).start();
    }

    public void resetHosnObeDeck(int numberOfPlayers) {
        hosnObeDeck = new HosnObeDeck(GameName.HosnObe, numberOfPlayers);
    }


    //Methode for mixing the Hosn Obe Game deck
    public void mixCard(){
        hosnObeDeck.mixCards();
        Responses.mixedCards responses = new Responses.mixedCards();
        lobby.getPlayerByID(1).getClientConnection().sendTCP(responses);
    }

    //Methode for handing out a Card
    public void handoutCard(){
        Card newCard;
        for (Player player : lobby.getPlayers()) {
            newCard = hosnObeDeck.takeCard();
            if (newCard != null) {
                player.addHandcard(newCard);

                // send message with handout card to players
                Responses.PlayerGetHandoutCard response = new Responses.PlayerGetHandoutCard();
                response.playerID = player.getClientConnection().getID();
                response.card = newCard;
                player.getClientConnection().sendTCP(response);
            }
        }
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
        for (Player player : lobby.getPlayers()) {
            // send message to client that game has started
            Responses.HosnObeStartedClientMessage request = new Responses.HosnObeStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
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
