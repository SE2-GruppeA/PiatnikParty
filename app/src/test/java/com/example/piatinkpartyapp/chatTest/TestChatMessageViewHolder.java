package com.example.piatinkpartyapp.chatTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import android.view.View;
import android.widget.TextView;

import com.example.piatinkpartyapp.chat.ChatAdapter;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.gamelogic.Player;

import org.junit.Test;

 public class TestChatMessageViewHolder {

    @Test
     void testConstructor() {
        View view = null;
        ChatAdapter.ChatMessageViewHolder chatMessageViewHolder
                = new ChatAdapter.ChatMessageViewHolder(view);
        assertNotNull(chatMessageViewHolder);
    }

     void testBindData() {
        Player player = new Player();
        ChatMessage chatMessage = new ChatMessage(
                player.getPlayerName(),
                "hallo",
                "2022-06-20",
                ChatMessage.MessageType.OUT);

        ChatAdapter.ChatMessageViewHolder chatMessageViewHolder
                = new ChatAdapter.ChatMessageViewHolder(null);

        TextView tvChatPlayerName;
        TextView tvChatMessage;
        TextView tvChatDate;
    }
}
