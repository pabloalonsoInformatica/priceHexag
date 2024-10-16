package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.prices.domain.Price;

import java.util.Arrays;

public class PricesByServiceSpecificationSearchResponseMother {
    public static PricesByServiceSpecificationSearchResponse  fromArrayPrices (Price[] originPrices){
        Price[] pricesArray = Arrays.stream(originPrices)
                .map(price -> Price.fromPrimitives(price.toPrimitives()))
                .toArray(Price[]::new);
        
        return new PricesByServiceSpecificationSearchResponse (pricesArray, (long) pricesArray.length);
    }
}