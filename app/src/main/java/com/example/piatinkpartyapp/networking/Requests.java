package com.example.piatinkpartyapp.networking;

import androidx.annotation.Nullable;

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

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof StartGameMessage){
                return true;
            }else {
                return false;
            }
        }
    }

    public static class PlayerSetCard {
        int playerID;
        Card card;

        public PlayerSetCard() { }

        public Card getCard(){
            return card;
        }

        public void setCard(Card card){
            this.card=card;
        }
        public PlayerSetCard(int playerID, Card card) {
            this.playerID = playerID;
            this.card = card;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof PlayerSetCard){
                PlayerSetCard comp = (PlayerSetCard) obj;
                if (playerID == comp.playerID && card.equals(comp.card)){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
    }

    public static class PlayerSetSchlag implements IPackets {
        CardValue schlag;

        public PlayerSetSchlag() { }

        public PlayerSetSchlag(CardValue schlag) {
            this.schlag = schlag;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj instanceof PlayerSetSchlag){
                PlayerSetSchlag comp = (PlayerSetSchlag) obj;
                if(schlag == comp.schlag){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public static class PlayerSetTrump implements IPackets {
        Symbol trump;

        public PlayerSetTrump() { }

        public PlayerSetTrump(Symbol trump) {
            this.trump = trump;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj instanceof PlayerSetTrump){
                PlayerSetTrump comp = (PlayerSetTrump) obj;
                if(trump == comp.trump){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
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
    public static class MixCardsRequest implements IPackets{
        public MixCardsRequest(){}
    }
}