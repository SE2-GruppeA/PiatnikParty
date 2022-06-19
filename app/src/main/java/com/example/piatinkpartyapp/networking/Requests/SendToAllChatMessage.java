package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.networking.IPackets;

public class SendToAllChatMessage implements IPackets {
    public String message;
    public int from;

    public SendToAllChatMessage() {
    }

    public SendToAllChatMessage(String message, int from) {
        this.message = message;
        this.from = from;
    }
}
