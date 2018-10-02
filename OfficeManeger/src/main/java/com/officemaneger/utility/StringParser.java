package com.officemaneger.utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{1,4})[.|\\/|-](\\d{1,2})[.|\\/|-](\\d{2,4})?");

    public static LocalDate getLocalDateFromString(String dateAsString) {
        if (dateAsString == null || dateAsString.trim().equals("")) {
            return null;
        }

        dateAsString = dateAsString.trim();
        int day = -1;
        int month = -1;
        int year = -1;

        String[] splittedInput = dateAsString.split("[\\/\\.-]");
        if (splittedInput.length == 2) {
            try {
                int first = Integer.valueOf(splittedInput[0]);
                int second = Integer.valueOf(splittedInput[1]);
                if (second > 12) {
                    month = first;
                    day = second;
                } else {
                    month = second;
                    day = first;
                }

                year = LocalDate.now().getYear();

                return LocalDate.of(year, month, day);
            } catch (Exception ex) {
                return null;
            }
        }

        Matcher dateMatcher = DATE_PATTERN.matcher(dateAsString);
        while (dateMatcher.find()) {
            String firstGr = dateMatcher.group(1);
            String middleGr = dateMatcher.group(2);
            String lastGr = dateMatcher.group(3);
            if (lastGr == null || lastGr.equals("")) {
                LocalDate now = LocalDate.now();
                String currentYear = "" + now.getYear();
                currentYear = currentYear.substring(2);
                lastGr = currentYear;
            }

            try {
                if (firstGr.length() > 2) {
                    day = Integer.parseInt(lastGr);
                    month = Integer.parseInt(middleGr);
                    year = Integer.parseInt(firstGr);
                } else {
                    day = Integer.parseInt(firstGr);
                    month = Integer.parseInt(middleGr);
                    year = Integer.parseInt(lastGr);
                }
            } catch (Exception ex) {
                return null;
            }
        }

        if (day < 1 || month < 1 || year < 1 || day > 31 || month > 12 || year > 2100) {
            return null;
        }

        LocalDate date = LocalDate.of(year, month, day);

        return date;
    }

    public static LocalTime getLocalTimeFromString(String timeAsString) {
        LocalTime time = null;
        String[] splitted = timeAsString.split("[-:]");
        if (splitted.length == 2) {
            int hour = -1;
            int minute = -1;
            try {
                hour = Integer.valueOf(splitted[0]);
                minute = Integer.valueOf(splitted[1]);

                if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                    time = LocalTime.of(hour, minute);
                }
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage());
            }
        }

        return time;
    }
}
