package com.example.piatinkpartyapp.networking.Responses;

import com.example.piatinkpartyapp.cards.PensionistlnRound;
import com.example.piatinkpartyapp.gamelogic.PensionistlnGame;
import com.example.piatinkpartyapp.networking.IPackets;

public class Response_PensionistlnStartedClientMessage implements IPackets {
    public PensionistlnRound round;

    public Response_PensionistlnStartedClientMessage() {
    }

    public Response_PensionistlnStartedClientMessage(PensionistlnRound round) {
        this.round = round;
    }
}
