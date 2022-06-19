package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class ReceiveToAllChatMessage implements IPackets {
    public String message;
    public String date;
    public int from;

    public ReceiveToAllChatMessage() {
    }

    public ReceiveToAllChatMessage(String message, int from, String date) {
        this.message = message;
        this.from = from;
        this.date = date;
    }
}
