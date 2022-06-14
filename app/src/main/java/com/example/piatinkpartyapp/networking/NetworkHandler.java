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
        kryo.register(Requests.PlayerSetSchlag.class);
        kryo.register(Requests.PlayerSetTrump.class);
        kryo.register(Requests.ForceVoting.class);
        kryo.register(Requests.VoteForNextGame.class);
        kryo.register(Requests.PlayerRequestsCheat.class);
        kryo.register(Requests.ExposePossibleCheater.class);
        kryo.register(Requests.MixCardsRequest.class);

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
        kryo.register(Responses.NotifyPlayerToSetSchlag.class);
        kryo.register(Responses.NotifyPlayerToSetTrump.class);
        kryo.register(Responses.SchnopsnStartedClientMessage.class);
        kryo.register(Responses.WattnStartedClientMessage.class);
        kryo.register(Responses.PensionistlnStartedClientMessage.class);
        kryo.register(Responses.HosnObeStartedClientMessage.class);
        kryo.register(Responses.IsCheater.class);
        kryo.register(Responses.SendRoundWinnerPlayerToAllPlayers.class);
        kryo.register(Responses.UpdateScoreboard.class);
        kryo.register(Responses.playerDisconnected.class);
        kryo.register(Responses.mixedCards.class);
        kryo.register(Responses.CheatingPenalty.class);


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
    }
}