package com.example.piatinkpartyapp.networking.responses;

import java.util.ArrayList;

public class responseEndOfRound {
    public ArrayList<Integer> playerIDs;

    public responseEndOfRound() {
    }

    public responseEndOfRound(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }
}
