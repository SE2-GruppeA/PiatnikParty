package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;

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

        kryo.register(Packets.Requests.SendHit.class);
        kryo.register(Packets.Requests.SendTrump.class);

        //Responses
        kryo.register(Packets.Responses.ConnectedSuccessfully.class);
        kryo.register(Packets.Responses.SendHandCards.class);
        kryo.register(Packets.Responses.NotifyPlayerYourTurn.class);
        kryo.register(Packets.Responses.PlayerGetHandoutCard.class);
        kryo.register(Packets.Responses.GameStartedClientMessage.class);
        kryo.register(Packets.Responses.EndOfRound.class);
        kryo.register(Packets.Responses.EndOfGame.class);

        kryo.register(Packets.Responses.getHit.class);
        kryo.register(Packets.Responses.getTrump.class);



        // Other classes
        kryo.register(Card.class);
        kryo.register(CardValue.class);
        kryo.register(Symbol.class);
        kryo.register(java.util.ArrayList.class);
    }
}