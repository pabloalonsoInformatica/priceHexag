package com.inditex.contexts.platform.prices.domain;

import com.inditex.contexts.platform.prices.domain.valueobjects.*;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.PriceDocument;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PriceMother {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    static Price create(
            PriceId id,
            PriceStartDate startDate,
            PriceEndDate endDate,
            PriceCurrency currency,
            PricePriority priority,
            PriceBrandId brandId,
            PriceProductId productId,
            PriceValue value
    ){
        return new Price(id, startDate, endDate, currency, priority, brandId, productId, value);
    }

    public static Price  fromPriceDocument (PriceDocument priceDocument){
        return  Price.fromPrimitives(
                Map.of(
                        "id", priceDocument.getId(),
                        "startDate", priceDocument.getStartDate().format(FORMATTER),
                        "endDate", priceDocument.getEndDate().format(FORMATTER),
                        "currency", priceDocument.getCurr(),
                        "priority", priceDocument.getPriority(),
                        "brandId", priceDocument.getBrand(),
                        "productId", priceDocument.getProduct(),
                        "value", priceDocument.getValue()
                )
        );
    }
}
