package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.IPackets;

public class Request_VoteForNextGame implements IPackets {
    public GameName gameName;

    public Request_VoteForNextGame() {
    }

    public Request_VoteForNextGame(GameName nextGame) {
        this.gameName = nextGame;
    }
}
