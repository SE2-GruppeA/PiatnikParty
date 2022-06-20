package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class responseServerMessage implements IPackets {
    public String message;

    public responseServerMessage(){

    }

    public responseServerMessage(String message){
        this.message = message;
    }
}
