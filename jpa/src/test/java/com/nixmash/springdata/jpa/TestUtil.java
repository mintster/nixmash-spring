package com.nixmash.springdata.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Petri Kainulainen
 */
public class TestUtil {

    public static String createUpdatedString(String value) {
        StringBuilder builder = new StringBuilder();

        builder.append(value);
        builder.append("Updated");

        return builder.toString();
    }

    public static Date date(int year, int month, int date) {
        Calendar working = GregorianCalendar.getInstance();
        working.set(year, month, date, 0, 0, 1);
        return working.getTime();
    }
}
