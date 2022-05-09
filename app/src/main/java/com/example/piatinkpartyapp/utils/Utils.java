package com.example.piatinkpartyapp.utils;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String getDateAsString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return "today at " + hours + ":" + minutes;
    }
}
