package com.example.piatinkpartyapp.networking.responses;

import com.example.piatinkpartyapp.cards.PensionistlnRound;
import com.example.piatinkpartyapp.networking.IPackets;

public class responsePensionistLnStartedClientMessage implements IPackets {
    public PensionistlnRound round;

    public responsePensionistLnStartedClientMessage() {
    }

    public responsePensionistLnStartedClientMessage(PensionistlnRound round) {
        this.round = round;
    }
}
