package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SchnopsnGame extends Game {

    private SchnopsnDeck deck;
    // Logging for testing
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public SchnopsnGame(Lobby l) {
        resetSchnopsnDeck();
        lobby = l;
    }

    public void resetSchnopsnDeck() {
        deck = new SchnopsnDeck(GameName.Schnopsn, 2);
    }

    // start the game
    @Override
    public void startGameSchnopsn() {
        new Thread(() -> {
            AusgabeTest();
            resetSchnopsnDeck();
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
            //  sendTrumpToAllPlayers(this.deck.getTrump());

            sendTrumpToAllPlayers(deck.getTrump());
            setRoundStartPlayer(lobby.getPlayers().get(0));
            // notify first player that it is his turn
            notifyPlayerYourTurn(lobby.getPlayers().get(0));
        }).start();
    }

    // send handcards to players
    public void sendHandCards() {
        for (Player player : lobby.getPlayers()) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            Responses.SendHandCards request = new Responses.SendHandCards();
            request.cards = handCards;
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
        }
    }

    // Player set card
    @Override
    public void setCard(int playerID, Card card) {
        Player player = lobby.getPlayerByID(playerID);

        //only for testing
        //Card card2 = player.getHandcards().get(0);

        new Thread(() -> {
            player.setRoundFinished(true);
            LOG.info("setRoundFinished = true");

            player.setCardPlayed(card);
            LOG.info("set Playercard of player: " + player.getId() + " card: " + card.getSymbol().toString() + card.getCardValue().toString());

            sendPlayedCardToAllPlayers(playerID, card);

            player.removeHandcard(card);
            LOG.info("card removed from handcards");

            if (checkIfAllPlayersFinishedRound()) {
                // gelegte Karten vergleichen und Stich zu cardsWon + Punkte dazurechnen
                LOG.info("RoundFinished. Trump is: " + deck.getTrump().toString());

                Player roundWonPlayer = getRoundWinnerPlayerSchnopsn();
                LOG.info("Round won by Player: " + roundWonPlayer.getId());

                addPointsToWinnerPlayer(roundWonPlayer);
                LOG.info("Points added to winner player: " + roundWonPlayer.getId() + ". Points: " + roundWonPlayer.getPoints());

                //TODO: check if one player have enough points
                startNewRoundSchnopsn(roundWonPlayer);
            } else {
                // Nächsten Spieler benachrichtigen dass er dran ist
                LOG.info("notify next player: " + getNextPlayer(player).getId());
                notifyPlayerYourTurn(getNextPlayer(player));
            }
        }).start();
    }

    public Player getRoundWinnerPlayerSchnopsn() {
        Player winnerPlayer = this.roundStartPlayer;
        Player currentPlayer = getNextPlayer(this.roundStartPlayer);

        while (currentPlayer != this.roundStartPlayer) {
            if (winnerPlayer.getCardPlayed().getSymbol() == deck.getTrump()) {
                if (currentPlayer.getCardPlayed().getSymbol() == deck.getTrump()
                        && deck.cardPoints(currentPlayer.getCardPlayed().getCardValue()) > deck.cardPoints(winnerPlayer.getCardPlayed().getCardValue())) {
                    winnerPlayer = currentPlayer;
                }
            } else {
                if (currentPlayer.getCardPlayed().getSymbol() == deck.getTrump()
                        || (winnerPlayer.getCardPlayed().getSymbol() == currentPlayer.getCardPlayed().getSymbol()
                        && deck.cardPoints(currentPlayer.getCardPlayed().getCardValue()) > deck.cardPoints(winnerPlayer.getCardPlayed().getCardValue()))) {
                    winnerPlayer = currentPlayer;
                }
            }
            currentPlayer = getNextPlayer(currentPlayer);
        }

        return winnerPlayer;
    }

    public void addPointsToWinnerPlayer(Player winnerPlayer) {
        for (Player player : lobby.getPlayers()) {
            winnerPlayer.addPoints(deck.cardPoints(player.getCardPlayed().getCardValue()));
            LOG.info("Points added to player: " + winnerPlayer.getId() + ". Points: " + deck.cardPoints(player.getCardPlayed().getCardValue()));
        }
        sendPointsToWinnerPlayer(winnerPlayer);
    }

    public void startNewRoundSchnopsn(Player startPlayer) {
        new Thread(() -> {
            if (startPlayer.getPoints() >= 66) {
                // if the player gets at least 66 points then the player wins
                // TODO: test if player can win on other places? wenn 20 oder 40 angesagt wird?
                sendEndRoundMessageToPlayers(startPlayer);
            } else if (startPlayer.getHandcards().isEmpty()) {
                // if handcards are empty, the player that won the last "Stich" wins the round
                sendEndRoundMessageToPlayers(startPlayer);
            } else {
                resetRoundFinished();
                resetPlayedCard();
                handoutCard();
                setRoundStartPlayer(startPlayer);
                notifyPlayerYourTurn(startPlayer);
            }
        }).start();
    }

    public void handoutCard() {
        Card newCard;
        for (Player player : lobby.getPlayers()) {
            newCard = deck.takeCard();
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
}
