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
import com.example.piatinkpartyapp.networking.Responses.CheatingPenalty;
import com.example.piatinkpartyapp.networking.Responses.ConnectedSuccessfully;
import com.example.piatinkpartyapp.networking.Responses.EndOfGame;
import com.example.piatinkpartyapp.networking.Responses.EndOfRound;
import com.example.piatinkpartyapp.networking.Responses.GameStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.HosnObeStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.IsCheater;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerToSetSchlag;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerToSetTrump;
import com.example.piatinkpartyapp.networking.Responses.NotifyPlayerYourTurn;
import com.example.piatinkpartyapp.networking.Responses.PensionistlnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.PlayerGetHandoutCard;
import com.example.piatinkpartyapp.networking.Responses.ReceiveEndToEndChatMessage;
import com.example.piatinkpartyapp.networking.Responses.ReceiveToAllChatMessage;
import com.example.piatinkpartyapp.networking.Responses.SchnopsnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.SendHandCards;
import com.example.piatinkpartyapp.networking.Responses.SendPlayedCardToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendRoundWinnerPlayerToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendSchlagToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.SendTrumpToAllPlayers;
import com.example.piatinkpartyapp.networking.Responses.UpdatePointsWinnerPlayer;
import com.example.piatinkpartyapp.networking.Responses.UpdateScoreboard;
import com.example.piatinkpartyapp.networking.Responses.WattnStartedClientMessage;
import com.example.piatinkpartyapp.networking.Responses.mixedCards;
import com.example.piatinkpartyapp.networking.Responses.playerDisconnected;
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
        kryo.register(ReceiveToAllChatMessage.class);

        kryo.register(Request_SendEndToEndChatMessage.class);
        kryo.register(ReceiveEndToEndChatMessage.class);

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
        kryo.register(ConnectedSuccessfully.class);
        kryo.register(SendHandCards.class);
        kryo.register(NotifyPlayerYourTurn.class);
        kryo.register(PlayerGetHandoutCard.class);
        kryo.register(GameStartedClientMessage.class);
        kryo.register(EndOfRound.class);
        kryo.register(EndOfGame.class);
        kryo.register(com.example.piatinkpartyapp.networking.Responses.VoteForNextGame.class);
        kryo.register(SendPlayedCardToAllPlayers.class);
        kryo.register(SendTrumpToAllPlayers.class);
        kryo.register(UpdatePointsWinnerPlayer.class);
        kryo.register(NotifyPlayerToSetSchlag.class);
        kryo.register(NotifyPlayerToSetTrump.class);
        kryo.register(SchnopsnStartedClientMessage.class);
        kryo.register(WattnStartedClientMessage.class);
        kryo.register(PensionistlnStartedClientMessage.class);
        kryo.register(HosnObeStartedClientMessage.class);
        kryo.register(IsCheater.class);
        kryo.register(SendRoundWinnerPlayerToAllPlayers.class);
        kryo.register(UpdateScoreboard.class);
        kryo.register(playerDisconnected.class);
        kryo.register(mixedCards.class);
        kryo.register(CheatingPenalty.class);
        kryo.register(SendSchlagToAllPlayers.class);

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