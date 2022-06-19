package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class ReceiveEndToEndChatMessage implements IPackets {
    public String message;
    public int from;
    public int to;

    public ReceiveEndToEndChatMessage() {
    }

    public ReceiveEndToEndChatMessage(String message, int from, int to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
