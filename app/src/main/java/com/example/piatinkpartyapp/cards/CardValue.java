package com.example.piatinkpartyapp.cards;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*Value a card can have*/
public enum CardValue {
    SIEBEN,
    ACHT,
    NEUN,
    ZEHN,
    UNTER,
    OBER,
    KOENIG, //"Ö" not possible due to resource accessing
    ASS;
    private static final List<CardValue> CARDVALUE_VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = CARDVALUE_VALUES.size();
    private static final Random RANDOM = new Random();

    public static CardValue randomValue(){
        return CARDVALUE_VALUES.get(RANDOM.nextInt(SIZE));
    }
}
