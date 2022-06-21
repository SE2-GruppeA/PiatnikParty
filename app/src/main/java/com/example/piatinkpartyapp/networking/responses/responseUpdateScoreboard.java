package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.networking.IPackets;

import java.util.Map;

public class responseUpdateScoreboard implements IPackets {
    public Map<String, Integer> players;

    public responseUpdateScoreboard() {
    }

    public responseUpdateScoreboard(Map<String, Integer> players) {
        this.players = players;
    }
}
