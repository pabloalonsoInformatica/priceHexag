package com.inditex.contexts.platform.prices.infrastructure.mocks;

import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.PriceDocument;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceDocumentMother {
    public static PriceDocument createDefault() {
        return new PriceDocument(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                "EUR",
                1,
                1L,
                1L,
                BigDecimal.valueOf(100),
                null
        );
    }
}
