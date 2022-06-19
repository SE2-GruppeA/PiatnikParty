package com.example.piatinkpartyapp.networking.Requests;

import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.networking.IPackets;

public class VoteForNextGame implements IPackets {
    public GameName gameName;

    public VoteForNextGame() {
    }

    public VoteForNextGame(GameName nextGame) {
        this.gameName = nextGame;
    }
}
