package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class Request_SendEndToEndChatMessage implements IPackets {
    public String message;
    public int from;
    public int to;

    public Request_SendEndToEndChatMessage() {
    }

    public Request_SendEndToEndChatMessage(String message, int from, int to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
