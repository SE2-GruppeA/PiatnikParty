package com.example.piatinkpartyapp.gamelogic;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.responses.responseWrongNumberOfPlayers;

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

    // for testing
    public Player addPlayer(int id, String playerName) {
        Player player = new Player(id, playerName);
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
        new Thread(()-> {
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
                        if (players.size() == 2) {
                            currentGame = new SchnopsnGame(this);
                            currentGame.startGame();
                        } else {
                            sendWrongNumberOfPlayersMessage("Schnopsn ist nur für 2 Spieler möglich!");
                        }
                        break;
                    case Wattn:
                        //start wattn
                        if (players.size() == 2 || players.size() == 3 || players.size() == 4) {
                            currentGame = new WattnGame(this);
                            currentGame.startGame();
                        } else {
                            sendWrongNumberOfPlayersMessage("Wattn ist nur für 2, 3 oder 4 Spieler möglich!");
                        }
                        break;
                    case HosnObe:
                        //start HosnObe
                        break;
                    case Pensionisteln:
                        // start pensionistln
                        if (players.size() == 4) {
                            currentGame = new PensionistlnGame(this);
                            currentGame.startGame();
                        } else {
                            sendWrongNumberOfPlayersMessage("Pensionistln ist nur für 4 Spieler möglich!");
                        }
                        break;
                    case endOfGame:
                        closeGame();
                    default:
                        break;
                }
            }
        }).start();
    }

    private void closeGame() {
        GameServer.getInstance().closeGame();
    }

    public GameName getWinnerGameOfVoting() {
        int countSchnopsn = 0;
        int countWattn = 0;
        int countHosnObe = 0;
        int countPensionisteln = 0;
        int countEndOfGame = 0;
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
                case endOfGame:
                    countEndOfGame++;
                    break;
                default:
                    break;
            }
        }

        // get max of votes
        int max = Math.max(Math.max(countSchnopsn, countWattn), Math.max(countEndOfGame, countPensionisteln));

        // return game with max votes
        if (countSchnopsn == max) {
            winnerGame = GameName.Schnopsn;
        } else if (countWattn == max) {
            winnerGame = GameName.Wattn;
        } else if (countHosnObe == max) {
            winnerGame = GameName.HosnObe;
        } else if (countEndOfGame == max){
            winnerGame = GameName.endOfGame;
        }else {
            winnerGame = GameName.Pensionisteln;
        }
        return winnerGame;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void sendWrongNumberOfPlayersMessage(String message) {
        responseWrongNumberOfPlayers response = new responseWrongNumberOfPlayers();
        response.message = message;

        for (Player player : players) {
            player.getClientConnection().sendTCP(response);
        }
    }
}
