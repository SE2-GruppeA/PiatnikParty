package com.example.piatinkpartyapp.networking.Responses;

import java.util.Map;

public class Response_UpdateScoreboard {
    public Map<String, Integer> players;

    public Response_UpdateScoreboard() {
    }

    public Response_UpdateScoreboard(Map<String, Integer> players) {
        this.players = players;
    }
}
