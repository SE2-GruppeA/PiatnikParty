package com.example.piatinkpartyapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {
    private ArrayList<ChatMessage> messages;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        holder.tv_playerName.setText(chatMessage.playerName);
        holder.tv_message.setText(chatMessage.message);
        holder.tv_time.setText(chatMessage.date);

        if(chatMessage.me){
            holder.cardView.setBackgroundColor(Color.parseColor("#cd5c5c"));
        }else{
            holder.cardView.setBackgroundColor(Color.parseColor("#ADD8E6"));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_playerName;
        private TextView tv_message;
        private TextView tv_time;
        private CardView cardView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_playerName = itemView.findViewById(R.id.tv_playerName);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
