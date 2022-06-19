package com.example.piatinkpartyapp.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum PensionistlnRound {
    KEIN_STICH, KEIN_HERZ, KEIN_OBER, KEIN_HERZ_KOENIG;

    private static final List<PensionistlnRound> ROUNDS = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = ROUNDS.size();
    private static final Random RANDOM = new Random();

    public static PensionistlnRound getRandomRound(){
        return ROUNDS.get(RANDOM.nextInt(SIZE));
    }
}
