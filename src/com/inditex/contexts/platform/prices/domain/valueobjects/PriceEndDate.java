package com.inditex.contexts.platform.prices.domain.valueobjects;

import com.inditex.contexts.shared.domain.valueobjects.DateTime;

import java.time.LocalDateTime;

public class PriceEndDate extends DateTime {

    public PriceEndDate(LocalDateTime value) {
        super(value);
    }

    public PriceEndDate(String value) {
        super(value);
    }
}
