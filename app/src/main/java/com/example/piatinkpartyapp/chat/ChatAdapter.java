package com.example.piatinkpartyapp.chat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageViewHolder> {
    private ArrayList<ChatMessage> messages;

    // used for coloring players CardView based on his playerName
    private static HashMap<String, String> playerToColorMapper;
    private static Stack<String> availablePlayerColors;

    public ChatAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
        initColor();
    }

    private void initColor() {
        playerToColorMapper = new HashMap<>();
        initAvailableColors();
    }

    public void initAvailableColors() {
        // color palette : https://colorhunt.co/palette
        availablePlayerColors = new Stack<>();
        availablePlayerColors.push("#7FB5FF");
        availablePlayerColors.push("#F24A72");
        availablePlayerColors.push("#FDAF75");
        availablePlayerColors.push("#EAEA7F");
    }

    private static void handlePlayerColor(String playerName, ChatMessageViewHolder holder) {
        String playerColor = null;
        if (playerToColorMapper.containsKey(playerName)) {
            playerColor = playerToColorMapper.get(playerName);
            holder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor(playerColor));
        } else {
            playerColor = availablePlayerColors.pop();
            playerToColorMapper.put(playerName, playerColor);
        }
        holder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor(playerColor));
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutID;
        switch (viewType) {
            case R.layout.fragment_item_chat_in:
                layoutID = R.layout.fragment_item_chat_in;
                break;
            case R.layout.fragment_item_chat_out:
                layoutID = R.layout.fragment_item_chat_out;
                break;
            default:
                throw new IllegalArgumentException("Invalid ViewType provided");
        }
        return new ChatMessageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        holder.bindData(messages.get(position), holder);
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getType() == ChatMessage.MessageType.IN)
            return R.layout.fragment_item_chat_in;
        return R.layout.fragment_item_chat_out;
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvChatPlayerName;
        private TextView tvChatMessage;
        private TextView tvChatDate;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            bindView();
        }

        private void bindView() {
            tvChatPlayerName = itemView.findViewById(R.id.tv_ChatPlayerName);
            tvChatMessage = itemView.findViewById(R.id.tv_chatMessage);
            tvChatDate = itemView.findViewById(R.id.tv_ChatDate);
        }

        public void bindData(ChatMessage chatMessage, ChatMessageViewHolder holder) {
            tvChatPlayerName.setText(chatMessage.getPlayerName());
            tvChatMessage.setText(chatMessage.getMessage());
            tvChatDate.setText(chatMessage.getDate());

            handlePlayerColor(tvChatPlayerName.getText().toString(), holder);
        }
    }
}