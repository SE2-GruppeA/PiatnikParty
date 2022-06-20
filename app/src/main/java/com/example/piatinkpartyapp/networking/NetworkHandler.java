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
import com.example.piatinkpartyapp.networking.responses.responseCheatingPenalty;
import com.example.piatinkpartyapp.networking.responses.responseConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.responses.responseEndOfGame;
import com.example.piatinkpartyapp.networking.responses.responseEndOfRound;
import com.example.piatinkpartyapp.networking.responses.responseGameStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseHosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseIsCheater;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.responses.responseNotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.responses.responsePensionistLnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responsePlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.responses.responseReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.responses.responseSchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseSendHandCards;
import com.example.piatinkpartyapp.networking.responses.responseSendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseSendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.responses.responseServerMessage;
import com.example.piatinkpartyapp.networking.responses.responseUpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.responses.responseUpdateScoreboard;
import com.example.piatinkpartyapp.networking.responses.responseVoteForNextGame;
import com.example.piatinkpartyapp.networking.responses.responseWattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.responses.responseMixedCards;
import com.example.piatinkpartyapp.networking.responses.responsePlayerDisconnected;
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
        kryo.register(responseReceiveToAllChatMessage.class);

        kryo.register(requestSendEndToEndChatMessage.class);
        kryo.register(responseReceiveEndToEndChatMessage.class);

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
        kryo.register(responseConnectedSuccessfully.class);
        kryo.register(responseSendHandCards.class);
        kryo.register(responseNotifyPlayerYourTurn.class);
        kryo.register(responsePlayerGetHandoutCard.class);
        kryo.register(responseGameStartedClientMessage.class);
        kryo.register(responseEndOfRound.class);
        kryo.register(responseEndOfGame.class);
        kryo.register(responseVoteForNextGame.class);
        kryo.register(responseSendPlayedCardToAllPlayers.class);
        kryo.register(responseSendTrumpToAllPlayers.class);
        kryo.register(responseUpdatePointsWinnerPlayer.class);
        kryo.register(responseNotifyPlayerToSetSchlag.class);
        kryo.register(responseNotifyPlayerToSetTrump.class);
        kryo.register(responseSchnopsnStartedClientMessage.class);
        kryo.register(responseWattnStartedClientMessage.class);
        kryo.register(responsePensionistLnStartedClientMessage.class);
        kryo.register(responseHosnObeStartedClientMessage.class);
        kryo.register(responseIsCheater.class);
        kryo.register(responseSendRoundWinnerPlayerToAllPlayers.class);
        kryo.register(responseUpdateScoreboard.class);
        kryo.register(responsePlayerDisconnected.class);
        kryo.register(responseMixedCards.class);
        kryo.register(responseCheatingPenalty.class);
        kryo.register(responseSendSchlagToAllPlayers.class);
        kryo.register(responseServerMessage.class);

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