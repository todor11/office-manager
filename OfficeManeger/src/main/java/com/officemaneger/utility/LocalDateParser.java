package com.officemaneger.utility;

import java.time.LocalDate;

public class LocalDateParser {

    public static String getLocalDateAsString(LocalDate date, String separator) {
        if (date == null) {
            return "";
        }

        int day = date.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = "0" + dayAsString;
        }

        int month = date.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = "0" + monthAsString;
        }

        return dayAsString + separator + monthAsString + separator + date.getYear();
    }
}
