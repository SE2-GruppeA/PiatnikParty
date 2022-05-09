package com.example.piatinkpartyapp.networking;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;

import java.security.PublicKey;
import java.util.ArrayList;

// will be used as some kind of placeholder for a generic packet send method!
interface IPackets{
    int x = 10;
}


/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an  empty constructor, else Kryonet throws an Exception !!!!
 */
public class Packets {

    public static class Requests{
        public static class SendEndToEndChatMessage implements IPackets {
            String message;
            int from;
            int to;

            public SendEndToEndChatMessage() {
            }

            public SendEndToEndChatMessage(String message, int from, int to) {
                this.message = message;
                this.from = from;
                this.to = to;
            }
        }

        public static class SendToAllChatMessage implements IPackets {
            String message;
            int from;

            public SendToAllChatMessage() {
            }

            public SendToAllChatMessage(String message, int from) {
                this.message = message;
                this.from = from;
            }
        }

        public static class StartGameMessage {
            public StartGameMessage() {
            }
        }

        public static class PlayerSetCard {
            int playerID;
            Card card;

            public PlayerSetCard() { }

            public PlayerSetCard(int playerID, Card card) {
                this.playerID = playerID;
                this.card = card;
            }
        }

        //for testing -> starts the voting process
        //in the final game this is not needed as at the end of each game
        //a vote will happen
        public static class ForceVoting implements IPackets {
            public ForceVoting() {}
        }

        public static class VoteForNextGame implements IPackets {
            GameName gameName;

            public VoteForNextGame() {}

            public VoteForNextGame(GameName nextGame) {
                this.gameName = nextGame;
            }
        }
    }

    public static class Responses {
        public static class ConnectedSuccessfully {
            int playerID;
            boolean isConnected;

            public ConnectedSuccessfully() { }

            public ConnectedSuccessfully(int playerID, boolean isConnected) {
                this.playerID = playerID;
                this.isConnected = isConnected;
            }

            public int getPlayerID() {
                return playerID;
            }

            public void setConnected(boolean connected) {
                isConnected = connected;
            }

            public void setPlayerID(int playerID) {
                this.playerID = playerID;
            }
        }

        public static class ReceiveEndToEndChatMessage implements IPackets {
            String message;
            int from;
            int to;

            public ReceiveEndToEndChatMessage() {
            }

            public ReceiveEndToEndChatMessage(String message, int from, int to) {
                this.message = message;
                this.from = from;
                this.to = to;
            }
        }
        public static class ReceiveToAllChatMessage implements IPackets {
            String message;
            String date;
            int from;

            public ReceiveToAllChatMessage() {
            }

            public ReceiveToAllChatMessage(String message, int from, String date) {
                this.message = message;
                this.from = from;
                this.date = date;
            }
        }

        public static class SendHandCards {
            public int playerID;
            public ArrayList<Card> cards;

            public SendHandCards() { }

            public SendHandCards(int playerID, ArrayList<Card> cards) {
                this.playerID = playerID;
                this.cards = cards;
            }
        }

        public static class NotifyPlayerYourTurn {
            public int playerID;

            public NotifyPlayerYourTurn() { }

            public NotifyPlayerYourTurn(int playerID) {
                this.playerID = playerID;
            }
        }

        public static class PlayerGetHandoutCard {
            public int playerID;
            public Card card;

            public PlayerGetHandoutCard() { }

            public PlayerGetHandoutCard(int playerID, Card card) {
                this.playerID = playerID;
                this.card = card;
            }
        }

        public static class GameStartedClientMessage {
            public GameStartedClientMessage() { }
        }

        public static class EndOfRound {
            public EndOfRound() { }
        }

        public static class EndOfGame {
            public EndOfGame() { }
        }

        public static class VoteForNextGame implements IPackets {
            public VoteForNextGame() { }
        }
    }
}
