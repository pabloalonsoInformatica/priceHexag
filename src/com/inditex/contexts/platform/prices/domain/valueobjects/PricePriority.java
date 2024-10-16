package com.inditex.contexts.platform.prices.domain.valueobjects;

import com.inditex.contexts.shared.domain.AppError;
import com.inditex.contexts.shared.domain.valueobjects.ValueObject;

public class PricePriority extends ValueObject<Integer> {

    public PricePriority(int value) {
        super(value);
        this.ensureIfItIsPositive(value);
    }

    private void ensureIfItIsPositive(int value) {
        if (value < 0) {
            throw new AppError("unsupported currency format");
        }
    }
}
