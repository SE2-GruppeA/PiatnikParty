package com.example.piatinkpartyapp.networking.requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class requestSendEndToEndChatMessage implements IPackets {
    public String message;
    public int from;
    public int to;

    public requestSendEndToEndChatMessage() {
    }

    public requestSendEndToEndChatMessage(String message, int from, int to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
