package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.chat.ChatMessage;

import org.junit.jupiter.api.Test;

public class TestChatMessage {

    @Test
    void constructorTestIn() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

    @Test
     void constructorTestOut() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.OUT;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

    @Test
     void constructorTestNull() {
        String playerName = null;
        String message = null;
        String date = null;
        ChatMessage.MessageType messageType = ChatMessage.MessageType.OUT;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertNotNull(chatMessage);
    }

    @Test
     void testGetPlayerName() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("player1", chatMessage.getPlayerName());
    }

    @Test
     void testGetMessage() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("Hallo", chatMessage.getMessage());
        assertNotEquals("lidhyshgfio", chatMessage.getMessage());
    }

    @Test
     void testGetDate() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        assertEquals("10-10-2022", chatMessage.getDate());
    }

    @Test
     void testGetType() {
        String playerName = "player1";
        String message = "Hallo";
        String date = "10-10-2022";
        ChatMessage.MessageType messageType = ChatMessage.MessageType.IN;
        ChatMessage chatMessage = new ChatMessage(playerName, message, date, messageType);
        ChatMessage.MessageType messageTypeTest = ChatMessage.MessageType.IN;
        assertEquals(messageTypeTest, chatMessage.getType());
    }

}
