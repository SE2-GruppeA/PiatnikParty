package com.example.piatinkpartyapp.cards;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*Card Symbols*/
public enum Symbol {
    HERZ,
    KARO,
    KREUZ,
    PICK;
    private static final List<Symbol> SYMBOL_VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = SYMBOL_VALUES.size();
    private static final Random RANDOM = new Random();

    public static Symbol randomSymbol(){
        return SYMBOL_VALUES.get(RANDOM.nextInt(SIZE));
    }
}
