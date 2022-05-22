package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Game {
    private int mainPlayerId;
    private Player roundStartPlayer;
    public ArrayList<Player> players = new ArrayList<>();
    private SchnopsnDeck deck;

    // Logging for testing
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public Game() {
        resetSchnopsnDeck();
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

    public Player getRoundStartPlayer() {
        return roundStartPlayer;
    }

    public void setRoundStartPlayer(Player roundStartPlayer) {
        this.roundStartPlayer = roundStartPlayer;
    }

    public void resetSchnopsnDeck() {
        deck = new SchnopsnDeck(GameName.Schnopsn, 2);
    }

    public Player addPlayer(Connection connection, String playerName) {
        Player player = new Player(connection, playerName);
        players.add(player);
        return player;
    }

    public Player getPlayerByID(int playerID) {
        for (Player player: players) {
            if (player.getId() == playerID) {
                return player;
            }
        }
        return new Player();
    }

    public boolean checkIfAllPlayersFinishedRound() {
        for (Player player: players) {
            if (!player.isRoundFinished()) {
                return false;
            }
        }
        return true;
    }

    public Player getNextPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        if (currentIndex == players.size()-1) {
            currentIndex = 0;
            //LOG.info("No next player! return first player");
        } else {
            currentIndex = currentIndex + 1;
            //LOG.info("Index of next player: " + currentIndex);
        }
        return players.get(currentIndex);
    }

    // reset points from players
    public void resetPlayerPoints() {
        for (Player player: players) {
            player.resetPoints();
        }
    }

    // reset played card from players
    public void resetPlayedCard() {
        for (Player player: players) {
            player.setCardPlayed(null);
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
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
            setRoundStartPlayer(players.get(0));
            // notify first player that it is his turn
            notifyPlayerYourTurn(players.get(0));
        }).start();
    }

    public void AusgabeTest() {
        LOG.info("ArrayList players: ");
        for (Player player : players) {
            LOG.info("Player: " + player.getId() + " - " + player.getPlayerName());
        }
    }

    // send handcards to players
    public void sendGameStartedMessageToClients() {
        for (Player player: players) {
            // send message to client that game has started
            Responses.GameStartedClientMessage request = new Responses.GameStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }

    // send handcards to players
    public void sendHandCards() {
        for (Player player: players) {
            ArrayList<Card> handCards = deck.getHandCards();
            player.setHandcards(handCards);

            // send message to client with handcards
            Responses.SendHandCards request = new Responses.SendHandCards();
            request.cards = handCards;
            request.playerID = player.getClientConnection().getID();
            player.getClientConnection().sendTCP(request);
        }
    }

    // notify player: "it's your turn"
    public void notifyPlayerYourTurn(Player player) {
        Responses.NotifyPlayerYourTurn request = new Responses.NotifyPlayerYourTurn();
        request.playerID =  player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    // Player set card
    public void setCard(int playerID, Card card) {
        Player player = getPlayerByID(playerID);

        //only for testing
        //Card card2 = player.getHandcards().get(0);

        new Thread(()->{
            player.setRoundFinished(true);
            LOG.info("setRoundFinished = true");

            player.setCardPlayed(card);
            LOG.info("set Playercard of player: " + player.getId() + " card: " +  card.getSymbol().toString() + card.getCardValue().toString());

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

        while(currentPlayer != this.roundStartPlayer) {
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
        for (Player player: players) {
            winnerPlayer.addPoints(deck.cardPoints(player.getCardPlayed().getCardValue()));
            LOG.info("Points added to player: " + winnerPlayer.getId() + ". Points: " + deck.cardPoints(player.getCardPlayed().getCardValue()));

        }
    }

    public void startNewRoundSchnopsn(Player startPlayer) {
        new Thread(()->{
            if (startPlayer.getPoints() >= 66 ) {
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
        for (Player player: players) {
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

    public void sendEndRoundMessageToPlayers(Player roundWinner) {
        for (Player player: players) {
            Responses.EndOfRound response = new Responses.EndOfRound();
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        for (Player player: players) {
            Responses.SendPlayedCardToAllPlayers response = new Responses.SendPlayedCardToAllPlayers();
            response.playerID = playerId;
            response.card = card;
            player.getClientConnection().sendTCP(response);
        }
    }
}
