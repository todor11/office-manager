package com.officemaneger.configs.springConverters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public final class LocalDateStringConverter implements Converter<LocalDate, String> {

    public LocalDateStringConverter() {
    }

    @Override
    public String convert(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        String year = "" + localDate.getYear();
        String month = "" + localDate.getMonth();
        String day = "" + localDate.getDayOfMonth();

        return day + "." + month + "." + year;
    }
}