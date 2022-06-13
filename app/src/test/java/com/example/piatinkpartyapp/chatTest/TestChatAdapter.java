package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.chat.ChatAdapter;
import com.example.piatinkpartyapp.chat.ChatMessage;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestChatAdapter {

    @Test
    public void testConstructor() {
        ArrayList<ChatMessage> messages;
        messages = null;
        ChatAdapter chatAdapter = new ChatAdapter(messages);
        assertNotNull(chatAdapter);
    }
}
