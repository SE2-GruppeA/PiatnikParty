package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.gamelogic.SchnopsnGame;
import com.example.piatinkpartyapp.gamelogic.WattnGame;
import com.example.piatinkpartyapp.networking.Responses.Response_CheatingPenalty;
import com.example.piatinkpartyapp.networking.Responses.Response_ConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.Responses.Response_EndOfGame;
import com.example.piatinkpartyapp.networking.Responses.Response_EndOfRound;
import com.example.piatinkpartyapp.networking.Responses.Response_GameStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_HosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_IsCheater;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.Responses.Response_NotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.Responses.Response_PensionistlnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_PlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.Responses.Response_ReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_ReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_SchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_SendHandCards;
import com.example.piatinkpartyapp.networking.Responses.Response_SendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_SendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdateScoreboard;
import com.example.piatinkpartyapp.networking.Responses.Response_VoteForNextGame;
import com.example.piatinkpartyapp.networking.Responses.Response_WattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_mixedCards;
import com.example.piatinkpartyapp.networking.Responses.Response_playerDisconnected;
import com.example.piatinkpartyapp.networking.Requests.Request_ExposePossibleCheater;
import com.example.piatinkpartyapp.networking.Requests.Request_ForceVoting;
import com.example.piatinkpartyapp.networking.Requests.Request_MixCardsRequest;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetCard;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetSchlag;
import com.example.piatinkpartyapp.networking.Requests.Request_PlayerSetTrump;
import com.example.piatinkpartyapp.networking.Requests.Request_SendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_SendToAllChatMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_StartGameMessage;
import com.example.piatinkpartyapp.networking.Requests.Request_VoteForNextGame;

import java.util.TreeMap;

public class NetworkHandler {

    //for testing now
    public static String GAMESERVER_IP = "127.0.0.1" ;
    //public static String GAMESERVER_IP = "192.168.1.3" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {


        kryo.register(Request_SendToAllChatMessage.class);
        kryo.register(Response_ReceiveToAllChatMessage.class);

        kryo.register(Request_SendEndToEndChatMessage.class);
        kryo.register(Response_ReceiveEndToEndChatMessage.class);

        //Requests
        kryo.register(Request_StartGameMessage.class);
        kryo.register(Request_PlayerSetCard.class);
        kryo.register(Request_PlayerSetSchlag.class);
        kryo.register(Request_PlayerSetTrump.class);
        kryo.register(Request_ForceVoting.class);
        kryo.register(Request_VoteForNextGame.class);
        kryo.register(Request_PlayerRequestsCheat.class);
        kryo.register(Request_ExposePossibleCheater.class);
        kryo.register(Request_MixCardsRequest.class);

        //Responses
        kryo.register(Response_ConnectedSuccessfully.class);
        kryo.register(Response_SendHandCards.class);
        kryo.register(Response_NotifyPlayerYourTurn.class);
        kryo.register(Response_PlayerGetHandoutCard.class);
        kryo.register(Response_GameStartedClientMessage.class);
        kryo.register(Response_EndOfRound.class);
        kryo.register(Response_EndOfGame.class);
        kryo.register(Response_VoteForNextGame.class);
        kryo.register(Response_SendPlayedCardToAllPlayers.class);
        kryo.register(Response_SendTrumpToAllPlayers.class);
        kryo.register(Response_UpdatePointsWinnerPlayer.class);
        kryo.register(Response_NotifyPlayerToSetSchlag.class);
        kryo.register(Response_NotifyPlayerToSetTrump.class);
        kryo.register(Response_SchnopsnStartedClientMessage.class);
        kryo.register(Response_WattnStartedClientMessage.class);
        kryo.register(Response_PensionistlnStartedClientMessage.class);
        kryo.register(Response_HosnObeStartedClientMessage.class);
        kryo.register(Response_IsCheater.class);
        kryo.register(Response_SendRoundWinnerPlayerToAllPlayers.class);
        kryo.register(Response_UpdateScoreboard.class);
        kryo.register(Response_playerDisconnected.class);
        kryo.register(Response_mixedCards.class);
        kryo.register(Response_CheatingPenalty.class);
        kryo.register(Response_SendSchlagToAllPlayers.class);

        // Other classes
        kryo.register(Card.class);
        kryo.register(CardValue.class);
        kryo.register(Symbol.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(GameName.class);
        kryo.register(Player.class);
        kryo.register(Connection.class);
        kryo.register(Server.class);
        kryo.register(Connection[].class);
        kryo.register(Lobby.class);
        kryo.register(SchnopsnGame.class);
        kryo.register(SchnopsnDeck.class);
        kryo.register(WattnGame.class);
        kryo.register(WattnDeck.class);
        kryo.register(TreeMap.class);
    }
}