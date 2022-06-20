package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class Response_ServerMessage implements IPackets {
    public String message;

    public Response_ServerMessage(){

    }

    public Response_ServerMessage(String message){
        this.message = message;
    }
}
