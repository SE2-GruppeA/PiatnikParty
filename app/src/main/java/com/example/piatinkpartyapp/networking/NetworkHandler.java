package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;

public class NetworkHandler {

    //for testing now
    public static String GAMESERVER_IP = "192.168.1.15" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {
        //Requests
        kryo.register(Packets.Requests.StartGameMessage.class);
        kryo.register(Packets.Requests.PlayerSetCard.class);

        //Responses
        kryo.register(Packets.Responses.ConnectedSuccessfully.class);
        kryo.register(Packets.Responses.SendHandCards.class);
        kryo.register(Packets.Responses.NotifyPlayerYourTurn.class);
        kryo.register(Packets.Responses.PlayerGetHandoutCard.class);
        kryo.register(Packets.Responses.GameStartedClientMessage.class);
        kryo.register(Packets.Responses.EndOfRound.class);
        kryo.register(Packets.Responses.EndOfGame.class);

        // Other classes
        kryo.register(Card.class);
        kryo.register(CardValue.class);
        kryo.register(Symbol.class);
        kryo.register(java.util.ArrayList.class);
    }
}