package com.officemaneger.configs.springConverters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class LocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(String source) {
        System.out.println("------------------------------------------");
        System.out.println(source);
        System.out.println("------------------------------------------");

        /*
        if (source == null || source.isEmpty()) {
            return null;
        }

        String[] splited = source.split("[\\.\\/-]");
        if (splited.length != 3) {
            return null;
        }

        String yearAsStr = splited[2];
        String monthAsStr = splited[1];
        String dayAsStr = splited[0];
        int year = 0;
        int month = 0;
        int day = 0;
        try {
            year = Integer.parseInt(yearAsStr);
            month = Integer.parseInt(monthAsStr);
            day = Integer.parseInt(dayAsStr);
        } catch (NumberFormatException nfe) {
            return null;
        }

        return LocalDate.of(year,month, day);
        */
        return LocalDate.parse(source, formatter);
    }
}