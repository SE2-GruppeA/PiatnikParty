package com.example.piatinkpartyapp.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageViewHolder> {
    private ArrayList<ChatMessage> messages;

    public ChatAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
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

        holder.bindData(messages.get(position));
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

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ChatPlayerName;
        private TextView tv_ChatMessage;
        private TextView tv_ChatDate;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            bindView();
        }

        private void bindView() {
            tv_ChatPlayerName = itemView.findViewById(R.id.tv_ChatPlayerName);
            tv_ChatMessage = itemView.findViewById(R.id.tv_chatMessage);
            tv_ChatDate = itemView.findViewById(R.id.tv_ChatDate);
        }

        public void bindData(ChatMessage chatMessage) {
            tv_ChatPlayerName.setText(chatMessage.playerName);
            tv_ChatMessage.setText(chatMessage.message);
            tv_ChatDate.setText(chatMessage.date);
        }
    }
}