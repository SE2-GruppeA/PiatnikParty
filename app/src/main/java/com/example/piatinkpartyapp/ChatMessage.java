package com.example.piatinkpartyapp;

public class ChatMessage {
    String playerName;
    String message;
    String date;
    Boolean me;

    public ChatMessage(String playerName, String message, String date, Boolean me) {
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.me = me;
    }
}
