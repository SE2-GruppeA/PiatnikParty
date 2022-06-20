package com.example.piatinkpartyapp.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.PensionistlnRound;
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
import com.example.piatinkpartyapp.networking.Responses.Response_ServerMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.Response_UpdateScoreboard;
import com.example.piatinkpartyapp.networking.Responses.Response_VoteForNextGame;
import com.example.piatinkpartyapp.networking.Responses.Response_WattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.Response_mixedCards;
import com.example.piatinkpartyapp.networking.Responses.Response_playerDisconnected;
import com.example.piatinkpartyapp.networking.requests.requestExposePossibleCheater;
import com.example.piatinkpartyapp.networking.requests.requestForceVoting;
import com.example.piatinkpartyapp.networking.requests.requestMixCardsRequest;
import com.example.piatinkpartyapp.networking.requests.requestPlayerRequestsCheat;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetCard;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetSchlag;
import com.example.piatinkpartyapp.networking.requests.requestPlayerSetTrump;
import com.example.piatinkpartyapp.networking.requests.requestSendEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestSendToAllChatMessage;
import com.example.piatinkpartyapp.networking.requests.requestStartGameMessage;
import com.example.piatinkpartyapp.networking.requests.requestVoteForNextGame;

import java.util.TreeMap;

public class NetworkHandler {

    //for testing now
    public static String GAMESERVER_IP = "127.0.0.1" ;
    //public static String GAMESERVER_IP = "192.168.1.3" ;
    static final int TCP_Port = 59555;
    static final int TCP_UDP = 54777;

    public static void register(Kryo kryo) {


        kryo.register(requestSendToAllChatMessage.class);
        kryo.register(Response_ReceiveToAllChatMessage.class);

        kryo.register(requestSendEndToEndChatMessage.class);
        kryo.register(Response_ReceiveEndToEndChatMessage.class);

        //Requests
        kryo.register(requestStartGameMessage.class);
        kryo.register(requestPlayerSetCard.class);
        kryo.register(requestPlayerSetSchlag.class);
        kryo.register(requestPlayerSetTrump.class);
        kryo.register(requestForceVoting.class);
        kryo.register(requestVoteForNextGame.class);
        kryo.register(requestPlayerRequestsCheat.class);
        kryo.register(requestExposePossibleCheater.class);
        kryo.register(requestMixCardsRequest.class);

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
        kryo.register(Response_ServerMessage.class);

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
        kryo.register(PensionistlnRound.class);
    }
}