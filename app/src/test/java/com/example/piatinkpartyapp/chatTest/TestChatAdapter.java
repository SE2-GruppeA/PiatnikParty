package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void testHandlePlayerColor() {

    }

    @Test
    void testGetItemViewType() {
        int position = 0;
    }

    @Test
    void testGetItemCount() {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        ChatAdapter chatAdapter = new ChatAdapter(messages);
        System.out.println(messages.size());
        assertEquals(0, chatAdapter.getItemCount());
    }
}
