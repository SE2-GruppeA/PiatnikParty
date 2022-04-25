package com.example.piatinkpartyapp.networking;

// will be used as some kind of placeholder for a generic packet send method!
interface IPackets{
}


import com.example.piatinkpartyapp.cards.Card;

import java.util.ArrayList;

/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an  empty constructor, else Kryonet throws an Exception !!!
 */
public class Packets {
    // Requests -> Message From Client to Server
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

        public static class SendToAllChatMessage {
            String message;
            int from;

            public SendToAllChatMessage() {
            }
        public static class StartGameMessage {
            public StartGameMessage(){
            }
        }
    }

    // Responses -> Message From Server to one Client or all Clients
    public static class Responses {
            public SendToAllChatMessage(String message, int from) {
                this.message = message;
                this.from = from;
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
            int from;

            public ReceiveToAllChatMessage() {
            }

            public ReceiveToAllChatMessage(String message, int from) {
                this.message = message;
                this.from = from;
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
    }

}