package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.Symbol;

public class NetworkHandler {

    //for testing now
    public static String GAMESERVER_IP = "127.0.0.1" ;
    //public static String GAMESERVER_IP = "192.168.1.3" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {


        kryo.register(Requests.SendToAllChatMessage.class);
        kryo.register(Responses.ReceiveToAllChatMessage.class);

        kryo.register(Requests.SendEndToEndChatMessage.class);
        kryo.register(Responses.ReceiveEndToEndChatMessage.class);

        //Requests
        kryo.register(Requests.StartGameMessage.class);
        kryo.register(Requests.PlayerSetCard.class);
        kryo.register(Requests.ForceVoting.class);
        kryo.register(Requests.VoteForNextGame.class);

        //Responses
        kryo.register(Responses.ConnectedSuccessfully.class);
        kryo.register(Responses.SendHandCards.class);
        kryo.register(Responses.NotifyPlayerYourTurn.class);
        kryo.register(Responses.PlayerGetHandoutCard.class);
        kryo.register(Responses.GameStartedClientMessage.class);
        kryo.register(Responses.EndOfRound.class);
        kryo.register(Responses.EndOfGame.class);
        kryo.register(Responses.VoteForNextGame.class);
        kryo.register(Responses.SendPlayedCardToAllPlayers.class);
        kryo.register(Responses.SendTrumpToAllPlayers.class);
        kryo.register(Responses.UpdatePointsWinnerPlayer.class);

        // Other classes
        kryo.register(Card.class);
        kryo.register(CardValue.class);
        kryo.register(Symbol.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(GameName.class);
    }
}