package com.inditex.contexts.platform.prices.domain.valueobjects;

import com.inditex.contexts.shared.domain.AppError;
import com.inditex.contexts.shared.domain.valueobjects.ValueObject;

import java.math.BigDecimal;

public class PriceValue extends ValueObject<BigDecimal> {

    public PriceValue(BigDecimal value) {
        super(value);
        this.ensurePositive(value);
    }

    private void ensurePositive(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new AppError("unsupported negative price");
        }
    }
}
