package com.inditex.apps.platform.backend.controllers.prices;

import com.inditex.apps.platform.backend.dtos.PriceHttpRequest;
import com.inditex.apps.platform.backend.dtos.PricesHttpResponse;
import com.inditex.contexts.platform.prices.application.searchbyspecification.PricesByServiceSpecificationSearchResponse;
import com.inditex.contexts.platform.prices.application.searchbyspecification.PricesBySpecificationSearch;
import com.inditex.contexts.platform.prices.application.searchbyspecification.PricesBySpecificationSearchQuery;
import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchQueryFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
public class PricesHttpRestGetHandler {
    private static final Logger logger = LoggerFactory.getLogger(PricesHttpRestGetHandler.class);
    private final PricesBySpecificationSearch pricesBySpecificationSearch;

    public PricesHttpRestGetHandler(PricesBySpecificationSearch pricesBySpecificationSearch) {
        this.pricesBySpecificationSearch = pricesBySpecificationSearch;
    }

    public ResponseEntity<PricesHttpResponse> run(PriceHttpRequest priceRequest) {
        logger.debug("PricesHttpRestGetHandler. priceRequest: {}", priceRequest);
        //compose query
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        Set<ServiceSpecificationSearchQueryFilter> filters = Set.of(
                new ServiceSpecificationSearchQueryFilter("product", priceRequest.getProductId().toString()),
                new ServiceSpecificationSearchQueryFilter("brand", priceRequest.getBrandId().toString()),
                new ServiceSpecificationSearchQueryFilter("date", priceRequest.getPriceDate().format(formatter))
        );

        //query
        PricesByServiceSpecificationSearchResponse result = this.pricesBySpecificationSearch.run(new PricesBySpecificationSearchQuery(filters));

        //compose answer
        if (result.getCount() > 0) {
            PricesHttpResponse response = PricesHttpResponse.from(result.getData(), result.getCount()).build();
            logger.debug("PricesHttpRestGetHandler. priceResponse: {}", response);
            logger.info("PricesHttpRestGetHandler. response OK");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("PricesHttpRestGetHandler. No price found for the given request. Returning 404.");
            return ResponseEntity.notFound().build();
        }
    }
}
