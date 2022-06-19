package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class SendEndToEndChatMessage implements IPackets {
    public String message;
    public int from;
    public int to;

    public SendEndToEndChatMessage() {
    }

    public SendEndToEndChatMessage(String message, int from, int to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
