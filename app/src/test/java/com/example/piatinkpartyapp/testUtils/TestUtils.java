package com.example.piatinkpartyapp.testUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.piatinkpartyapp.utils.Utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

class TestUtils {

    @Test
    void testGetDateAsString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        assertEquals("today at " + hours + ":" + minutes, Utils.getDateAsString());
    }

}
