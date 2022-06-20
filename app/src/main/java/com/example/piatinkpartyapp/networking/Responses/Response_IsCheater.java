package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.networking.IPackets;

public class Response_IsCheater implements IPackets {
    public boolean isCheater;

    public Response_IsCheater() {
    }

    public Response_IsCheater(boolean isCheater) {
        this.isCheater = isCheater;
    }
}
