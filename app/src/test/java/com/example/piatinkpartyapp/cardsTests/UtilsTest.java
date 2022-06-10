package com.example.piatinkpartyapp.cardsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.esotericsoftware.kryonet.Connection;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.gamelogic.Lobby;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.utils.Utils;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.robolectric.util.Util;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class UtilsTest {

    @Test
    public void TestTestGetDateAsString(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        String date = "today at " + hours + ":" + minutes;

        assertEquals(date,Utils.getDateAsString());
    }
}
