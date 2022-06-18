package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.GameServer;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Lobby {
    public ArrayList<Player> players = new ArrayList<>();
    public Player host;
    public Game currentGame;

    // Logging for testing
    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

    public ArrayList<Player> getPlayers() {
        return players;
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

    public boolean checkIfAllPlayersFinishedVoting() {
        for (Player player : players) {
            if (!player.isVotingFinished()) {
                return false;
            }
        }
        return true;
    }

    // reset roundFinished from players
    public void resetVotingFinished() {
        for (Player player : players) {
            player.setVotingFinished(false);
        }
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
                    currentGame = new SchnopsnGame(this);
                    currentGame.startGame();
                    break;
                case Wattn:
                    //start wattn
                    currentGame = new WattnGame(this);
                    currentGame.startGame();
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
