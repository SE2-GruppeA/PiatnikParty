package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class responseReceiveToAllChatMessage implements IPackets {
    public String message;
    public String date;
    public int from;

    public responseReceiveToAllChatMessage() {
    }

    public responseReceiveToAllChatMessage(String message, int from, String date) {
        this.message = message;
        this.from = from;
        this.date = date;
    }
}
