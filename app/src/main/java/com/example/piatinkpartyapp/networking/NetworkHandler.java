package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.example.piatinkpartyapp.cards.Card;

public class NetworkHandler {

    //for testing now
    public static String GAMESERVER_IP = "127.0.0.1" ;
    //public static String GAMESERVER_IP = "192.168.1.3" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {


        kryo.register(Packets.Requests.SendToAllChatMessage.class);
        kryo.register(Packets.Responses.ReceiveToAllChatMessage.class);

        kryo.register(Packets.Requests.SendEndToEndChatMessage.class);
        kryo.register(Packets.Responses.ReceiveEndToEndChatMessage.class);

        //Requests
        kryo.register(Packets.Requests.StartGameMessage.class);
        kryo.register(Packets.Requests.PlayerSetCard.class);

        //Responses
        kryo.register(Packets.Responses.ConnectedSuccessfully.class);
        kryo.register(Packets.Responses.SendHandCards.class);
        kryo.register(Packets.Responses.NotifyPlayerYourTurn.class);
        kryo.register(Packets.Responses.PlayerGetHandoutCard.class);

        // Other classes
        kryo.register(Card.class);
    }
}