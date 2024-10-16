package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.prices.domain.Price;
import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchResponse;

public class PricesByServiceSpecificationSearchResponse extends ServiceSpecificationSearchResponse<Price> {
    public PricesByServiceSpecificationSearchResponse(Price[] data, Long count) {
        super(data, count);
    }
}
