package com.example.piatinkpartyapp.networking.Responses;

public class Response_SendRoundWinnerPlayerToAllPlayers {
    public int winnerPlayerID;

    public Response_SendRoundWinnerPlayerToAllPlayers() {
    }

    public Response_SendRoundWinnerPlayerToAllPlayers(int winnerPlayerID) {
        this.winnerPlayerID = winnerPlayerID;
    }
}
