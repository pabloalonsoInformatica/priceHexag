package com.inditex.contexts.platform.prices.infrastructure.mocks.h2;

import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.PriceDocument;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

public class PricePageMother {

    public static PageImpl createOneResult (PriceDocument priceDocument) {
        return new PageImpl<>(
                Collections.singletonList(priceDocument),
                PageRequest.of(0, 1),
                1
        );
    }

    public static PageImpl createNoResults () {
        return new PageImpl<>(Collections.emptyList());
    }
}