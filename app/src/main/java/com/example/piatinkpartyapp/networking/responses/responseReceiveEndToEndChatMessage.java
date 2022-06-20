package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class responseReceiveEndToEndChatMessage implements IPackets {
    public String message;
    public int from;
    public int to;

    public responseReceiveEndToEndChatMessage() {
    }

    public responseReceiveEndToEndChatMessage(String message, int from, int to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
