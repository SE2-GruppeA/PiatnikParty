package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.piatinkpartyapp.cards.PensionistlnRound;

import org.junit.jupiter.api.Test;

class TestPensionistlnRound {

    private enum PensionistlnRoundEnum {
        KEIN_STICH,
        KEIN_HERZ,
        KEIN_OBER,
        KEIN_HERZ_KOENIG;
    }

    @Test
    void testPensionistlnRound() {
        PensionistlnRound pensionistlnRound = PensionistlnRound.getRandomRound();
        assertNotNull(pensionistlnRound);
    }
}
