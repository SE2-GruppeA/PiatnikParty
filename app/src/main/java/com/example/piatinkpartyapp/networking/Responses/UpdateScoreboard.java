package com.example.piatinkpartyapp.networking.Responses;

import java.util.Map;

public class UpdateScoreboard {
    public Map<String, Integer> players;

    public UpdateScoreboard() {
    }

    public UpdateScoreboard(Map<String, Integer> players) {
        this.players = players;
    }
}
