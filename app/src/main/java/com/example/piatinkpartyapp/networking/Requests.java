package com.example.piatinkpartyapp.networking;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;

/*
NOTES :
    If you create a constructor, which also sets the variables,
    then we also need an  empty constructor, else Kryonet throws an Exception !!!!
 */

public class Requests{
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

    public static class PlayerSetSchlag implements IPackets {
        CardValue schlag;

        public PlayerSetSchlag() { }

        public PlayerSetSchlag(CardValue schlag) {
            this.schlag = schlag;
        }
    }

    public static class PlayerSetTrump implements IPackets {
        Symbol trump;

        public PlayerSetTrump() { }

        public PlayerSetTrump(Symbol trump) {
            this.trump = trump;
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

    public static class PlayerRequestsCheat implements IPackets {
        public PlayerRequestsCheat(){

        }
    }
}