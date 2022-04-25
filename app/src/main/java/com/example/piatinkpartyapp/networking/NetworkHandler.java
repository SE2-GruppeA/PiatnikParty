package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;

class NetworkHandler {
    //for testing now!
    public static String GAMESERVER_IP = "192.168.1.15" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {
        kryo.register(Packets.Responses.ConnectedSuccessfully.class);
    }
}