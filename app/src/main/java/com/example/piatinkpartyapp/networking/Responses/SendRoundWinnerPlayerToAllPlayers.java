package com.example.piatinkpartyapp.networking.Responses;

public class SendRoundWinnerPlayerToAllPlayers {
    public int winnerPlayerID;

    public SendRoundWinnerPlayerToAllPlayers() {
    }

    public SendRoundWinnerPlayerToAllPlayers(int winnerPlayerID) {
        this.winnerPlayerID = winnerPlayerID;
    }
}
