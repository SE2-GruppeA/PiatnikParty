package com.example.piatinkpartyapp.networking.requests;

import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.IPackets;

public class requestVoteForNextGame implements IPackets {
    public GameName gameName;

    public requestVoteForNextGame() {
    }

    public requestVoteForNextGame(GameName nextGame) {
        this.gameName = nextGame;
    }
}
