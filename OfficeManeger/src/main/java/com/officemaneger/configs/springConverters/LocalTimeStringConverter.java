package com.officemaneger.configs.springConverters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class LocalTimeStringConverter implements Converter<LocalTime, String> {

    public LocalTimeStringConverter() {
    }

    @Override
    public String convert(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        String hour = "" + localTime.getHour();
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String minute = "" + localTime.getMinute();
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return hour + ":" + minute;
    }
}