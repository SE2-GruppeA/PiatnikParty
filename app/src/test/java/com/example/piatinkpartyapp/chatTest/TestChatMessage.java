package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.chat.ChatMessage;

import org.junit.jupiter.api.Test;

public class TestChatMessage {

    @Test
    public void constructorTestIn() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

    @Test
    public void constructorTestOut() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.OUT;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

    @Test
    public void constructorTestNull() {
        String playerName = null;
        String message = null;
        String date = null;
        ChatMessage.MessageType messageType = ChatMessage.MessageType.OUT;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

}
