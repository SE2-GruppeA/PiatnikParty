package com.example.piatinkpartyapp.gamelogic;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.responses.responseMixedCards;
import com.example.piatinkpartyapp.networking.responses.responsePlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.responses.responseSendHandCards;

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

    @Override
    // start the game
    public void startGame() {
        new Thread(() -> {
            AusgabeTest();
            resetSchnopsnDeck();
            sendMessageUpdateScoreboard();
            sendGameStartedMessageToClients();
            resetRoundFinished();
            resetPlayerPoints();
            resetCheating();
            sendHandCards();

            sendTrumpToAllPlayers(deck.getTrump());

            Player roundStartPlayer = getRandomPlayer();
            setRoundStartPlayer(roundStartPlayer);
            // notify first player that it is his turn
            notifyPlayerYourTurn(roundStartPlayer);
        }).start();
    }

    // send handcards to players
    public void sendHandCards() {
        for (Player player : lobby.getPlayers()) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
           sendHandCardsToPlayer(handCards, player);
        }
    }

    public void sendHandCardsToPlayer(ArrayList<Card> handCards, Player player){
        responseSendHandCards request = new responseSendHandCards();
        request.cards = handCards;
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    //used for cheating; gives player best card
    @Override
    public void givePlayerBestCard(int playerId) {
        lobby.getPlayerByID(playerId).setCheaten(true);
        switch (deck.getTrump()){
            case HERZ:
                sendPlayerBestCard(playerId, new Card(Symbol.HERZ, CardValue.ASS));
                break;
            case PICK:
                sendPlayerBestCard(playerId, new Card(Symbol.PICK, CardValue.ASS));
                break;
            case KARO:
                sendPlayerBestCard(playerId, new Card(Symbol.KARO, CardValue.ASS));
                break;
            case KREUZ:
                sendPlayerBestCard(playerId, new Card(Symbol.KREUZ, CardValue.ASS));
                break;
        }
    }

    //replaces the first hand card with the best card in the game
    public void sendPlayerBestCard(int playerId, Card card){
        Player player = lobby.getPlayerByID(playerId);

        ArrayList<Card> currentHandCards = player.getHandcards();

        //replaces first card with the best card
        currentHandCards.set(0, card);
        player.setHandcards(currentHandCards);

        //sends new handcards to the player
        sendHandCardsToPlayer(currentHandCards, player);
    }

    // Player set card
    @Override
    public void setCard(int playerID, Card card) {
        Player player = lobby.getPlayerByID(playerID);

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

                sendRoundWinnerPlayerToAllPlayers(roundWonPlayer);

                startNewRoundSchnopsn(roundWonPlayer);
            } else {
                // N??chsten Spieler benachrichtigen dass er dran ist
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
                        && deck.cardPoints(currentPlayer.getCardPlayed().getCardValue())
                        > deck.cardPoints(winnerPlayer.getCardPlayed().getCardValue())) {
                    winnerPlayer = currentPlayer;
                }
            } else {
                if (currentPlayer.getCardPlayed().getSymbol() == deck.getTrump()
                        || (winnerPlayer.getCardPlayed().getSymbol() == currentPlayer.getCardPlayed().getSymbol()
                        && deck.cardPoints(currentPlayer.getCardPlayed().getCardValue())
                        > deck.cardPoints(winnerPlayer.getCardPlayed().getCardValue()))) {
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
            if (startPlayer.getPoints() >= 66 || startPlayer.getHandcards().isEmpty()) {
                // if the player gets at least 66 points then the player wins or if handcards are empty, the player that won the last "Stich" wins the round
                ArrayList<Player> winner = new ArrayList<>();
                winner.add(startPlayer);

                sendEndRoundMessageToPlayers(winner);
                addPointsAndUpdateScoreboard(startPlayer, 1);
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
                responsePlayerGetHandoutCard response = new responsePlayerGetHandoutCard();
                response.playerID = player.getClientConnection().getID();
                response.card = newCard;
                player.getClientConnection().sendTCP(response);
            }
        }
    }

    //mix cards in deck
    @Override
    public void mixCards(){
        LOG.info("before mixing");
        LOG.info(deck.getDeck().get(0).toString());
        deck.mixCards();
        LOG.info("after mixing");
        LOG.info(deck.getDeck().get(0).toString());
        LOG.info(deck.toString());
        responseMixedCards response = new responseMixedCards();
        lobby.getPlayerByID(1).getClientConnection().sendTCP(response);
    }

    public SchnopsnDeck getDeck() {
        return deck;
    }

    public void setDeck(SchnopsnDeck deck){
        this.deck = deck;
    }
}
