package com.inditex.contexts.shared.domain.valueobjects;

import com.inditex.contexts.shared.domain.AppError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateTime extends ValueObject<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    public DateTime(LocalDateTime value) {
        super(value.truncatedTo(ChronoUnit.SECONDS));
    }

    public DateTime(String value) {
        super(parseDate(value));
    }

    private static LocalDateTime parseDate(String value) {
        try {
            return LocalDateTime.parse(value, formatter).truncatedTo(ChronoUnit.SECONDS);
        } catch (DateTimeParseException e) {
            throw new AppError("dateTime no valid");
        }
    }

    @Override
    public String toString() {
        return this.value.truncatedTo(ChronoUnit.SECONDS).format(formatter);

    }
}