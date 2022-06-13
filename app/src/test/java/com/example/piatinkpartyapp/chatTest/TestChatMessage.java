package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @Test
    public void testGetPlayerName() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("player1", chatMessage.getPlayerName());
    }

    @Test
    public void testGetMessage() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("Hallo", chatMessage.getMessage());
        assertNotEquals("lidhyshgfio", chatMessage.getMessage());
    }

    @Test
    public void testGetDate() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("10-10-2022", chatMessage.getDate());
    }

    @Test
    public void testGetType() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        ChatMessage.MessageType messageTypeTest = ChatMessage.MessageType.IN;
        assertEquals(messageTypeTest, chatMessage.getType());
    }

}
