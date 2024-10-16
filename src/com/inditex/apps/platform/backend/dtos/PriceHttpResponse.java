package com.inditex.apps.platform.backend.dtos;

import com.inditex.contexts.platform.prices.domain.Price;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PriceHttpResponse {
    private Long priceList;
    private Long productId;
    private Long brandId;
    private String startDate;
    private String endDate;
    private BigDecimal value;

    public static PriceHttpResponseBuilder from(Price price) {
        Map<String, Object> primitives = price.toPrimitives();
        return PriceHttpResponse.builder()
                .priceList((Long) primitives.get("id"))
                .productId((Long) primitives.get("productId"))
                .brandId((Long) primitives.get("brandId"))
                .startDate((String) primitives.get("startDate"))
                .endDate((String) primitives.get("endDate"))
                .value((BigDecimal) primitives.get("value"));
    }
}
