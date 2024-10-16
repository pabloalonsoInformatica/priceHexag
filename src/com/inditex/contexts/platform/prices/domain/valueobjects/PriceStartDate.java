package com.inditex.contexts.platform.prices.domain.valueobjects;

import com.inditex.contexts.shared.domain.valueobjects.DateTime;

import java.time.LocalDateTime;


public class PriceStartDate extends DateTime {

    public PriceStartDate(LocalDateTime value) {
        super(value);
    }

    public PriceStartDate(String value) {
        super(value);
    }

}
