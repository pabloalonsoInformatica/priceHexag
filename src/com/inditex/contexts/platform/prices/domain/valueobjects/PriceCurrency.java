package com.inditex.contexts.platform.prices.domain.valueobjects;

import com.inditex.contexts.shared.domain.AppError;
import com.inditex.contexts.shared.domain.valueobjects.ValueObject;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class PriceCurrency extends ValueObject<String> {

    private static final Set<String> VALID_CODES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    public PriceCurrency(String value) {
        super(value);
        this.ensureIso4217(value);
    }

    private void ensureIso4217(String value){
        if(!VALID_CODES.contains(value)){
            throw new AppError("unsupported currency format");
        }
    }
}
