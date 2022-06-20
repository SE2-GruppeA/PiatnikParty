package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class Response_ReceiveToAllChatMessage implements IPackets {
    public String message;
    public String date;
    public int from;

    public Response_ReceiveToAllChatMessage() {
    }

    public Response_ReceiveToAllChatMessage(String message, int from, String date) {
        this.message = message;
        this.from = from;
        this.date = date;
    }
}
