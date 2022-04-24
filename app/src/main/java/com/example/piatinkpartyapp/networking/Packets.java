package com.example.piatinkpartyapp.networking;


import com.example.piatinkpartyapp.cards.Card;

import java.util.ArrayList;

/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an empty constructor, else Kryonet throws an Exception !!!
 */
public class Packets {

    public static class Requests{

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

        //Empty message
        public static class LobbyCreatedMessage {
            public LobbyCreatedMessage(){
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