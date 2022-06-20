package com.example.piatinkpartyapp.networking.responses;

import java.util.Map;

public class responseUpdateScoreboard {
    public Map<String, Integer> players;

    public responseUpdateScoreboard() {
    }

    public responseUpdateScoreboard(Map<String, Integer> players) {
        this.players = players;
    }
}
