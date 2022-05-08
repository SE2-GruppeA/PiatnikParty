package com.example.piatinkpartyapp.chat;

public class ChatMessage {
    String playerName;
    String message;
    String date;
    MessageType type;

    public ChatMessage(String playerName, String message, String date, MessageType type) {
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.type = type;

    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public MessageType getType() {
        return type;
    }

    public enum MessageType {
        IN, OUT
    }
}
