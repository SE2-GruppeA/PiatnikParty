package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class Request_SendToAllChatMessage implements IPackets {
    public String message;
    public int from;

    public Request_SendToAllChatMessage() {
    }

    public Request_SendToAllChatMessage(String message, int from) {
        this.message = message;
        this.from = from;
    }
}
