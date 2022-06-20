package com.example.piatinkpartyapp.networking.requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class requestSendToAllChatMessage implements IPackets {
    public String message;
    public int from;

    public requestSendToAllChatMessage() {
    }

    public requestSendToAllChatMessage(String message, int from) {
        this.message = message;
        this.from = from;
    }
}
