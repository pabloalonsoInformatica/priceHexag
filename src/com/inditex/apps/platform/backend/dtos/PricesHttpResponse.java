package com.inditex.apps.platform.backend.dtos;

import com.inditex.contexts.platform.prices.domain.Price;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PricesHttpResponse {
    private List<PriceHttpResponse> prices;
    private Long count = 0L;

    public static PricesHttpResponseBuilder from (Price[] prices, Long count) {
        List<PriceHttpResponse> priceResponses = Arrays.stream(prices)
                .map(PriceHttpResponse::from)
                .map(PriceHttpResponse.PriceHttpResponseBuilder::build)
                .toList();
        return PricesHttpResponse.builder()
                .prices(priceResponses)
                .count(count);
    }
}
