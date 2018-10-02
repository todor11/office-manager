package com.officemaneger.configs.springConverters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class LocalTimeConverter implements Converter<String, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeConverter(String timeFormat) {
        this.formatter = DateTimeFormatter.ofPattern(timeFormat);
    }

    @Override
    public LocalTime convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return LocalTime.parse(source, formatter);
    }
}