package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
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
        for (Player player : players) {
            if (player.getId() == playerID) {
                return player;
            }
        }
        return new Player();
    }

    public boolean checkIfAllPlayersFinishedRound() {
        for (Player player : players) {
            if (!player.isRoundFinished()) {
                return false;
            }
        }
        return true;
    }

    public Player getNextPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        if (currentIndex == players.size() - 1) {
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
        for (Player player : players) {
            player.resetPoints();
        }
    }

    // reset played card from players
    public void resetPlayedCard() {
        for (Player player : players) {
            player.setCardPlayed(null);
        }
    }

    // reset roundFinished from players
    public void resetRoundFinished() {
        for (Player player : players) {
            player.setRoundFinished(false);
        }
    }

    // reset roundFinished from players
    public void resetVotingFinished() {
        for (Player player : players) {
            player.setVotingFinished(false);
        }
    }

    // start the game
    public void startGameSchnopsn() {
        new Thread(() -> {
            AusgabeTest();
           // resetSchnopsnDeck();
            deck = new SchnopsnDeck(GameName.Schnopsn,2);
            sendGameStartedMessageToClients();
            resetRoundFinished();
            sendHandCards();
          //  sendTrumpToAllPlayers(this.deck.getTrump());

            sendTrumpToAllPlayers(deck.getTrump());
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
        for (Player player : players) {
            // send message to client that game has started
            Responses.GameStartedClientMessage request = new Responses.GameStartedClientMessage();
            player.getClientConnection().sendTCP(request);
        }
    }

    // send handcards to players
    public void sendHandCards() {
        for (Player player : players) {
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
        request.playerID = player.getClientConnection().getID();
        player.getClientConnection().sendTCP(request);
    }

    // Player set card
    public void setCard(int playerID, Card card) {
        Player player = getPlayerByID(playerID);

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
        for (Player player : players) {
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
        for (Player player : players) {
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
        for (Player player : players) {
            Responses.EndOfRound response = new Responses.EndOfRound();
            response.playerID = roundWinner.getId();
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendPlayedCardToAllPlayers(int playerId, Card card) {
        for (Player player : players) {
            Responses.SendPlayedCardToAllPlayers response = new Responses.SendPlayedCardToAllPlayers();
            response.playerID = playerId;
            response.card = card;
            player.getClientConnection().sendTCP(response);
        }
    }

    public void sendTrumpToAllPlayers(Symbol symbol) {
        for (Player player : players) {
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

    public boolean checkIfAllPlayersFinishedVoting() {
        for (Player player : players) {
            if (!player.isVotingFinished()) {
                return false;
            }
        }
        return true;
    }

    public void handleVotingForNextGame(int playerID, GameName gameVoted) {
        Player player = getPlayerByID(playerID);

        player.setVotingFinished(true);
        player.setVotingGame(gameVoted);

        if (checkIfAllPlayersFinishedVoting()) {
            resetVotingFinished();
            GameName winnerGame = getWinnerGameOfVoting();
            LOG.info("Voting won by game: " + winnerGame.toString());
            switch (winnerGame) {
                case Schnopsn:
                    //start schnopsn
                    startGameSchnopsn();
                    break;
                case Wattn:
                    //start wattn
                    break;
                case HosnObe:
                    //start HosnObe
                    break;
                case Pensionisteln:
                    // start pensionistln
                    break;
                default:
                    break;
            }
        }
    }

    public GameName getWinnerGameOfVoting() {
        int countSchnopsn = 0;
        int countWattn = 0;
        int countHosnObe = 0;
        int countPensionisteln = 0;
        GameName winnerGame;

        for (Player player : players) {
            GameName gameVoted = player.getVotingGame();
            switch (gameVoted) {
                case Schnopsn:
                    countSchnopsn = countSchnopsn + 1;
                    break;
                case Wattn:
                    countWattn = countWattn + 1;
                    break;
                case HosnObe:
                    countHosnObe = countHosnObe + 1;
                    break;
                case Pensionisteln:
                    countPensionisteln = countPensionisteln + 1;
                    break;
                default:
                    break;
            }
        }

        // get max of votes
        int max = Math.max(Math.max(countSchnopsn, countWattn), Math.max(countHosnObe, countPensionisteln));

        // return game with max votes
        if (countSchnopsn == max) {
            winnerGame = GameName.Schnopsn;
        } else if (countWattn == max) {
            winnerGame = GameName.Wattn;
        } else if (countHosnObe == max) {
            winnerGame = GameName.HosnObe;
        } else {
            winnerGame = GameName.Pensionisteln;
        }
        return winnerGame;
    }
}
