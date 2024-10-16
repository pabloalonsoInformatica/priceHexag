package com.inditex.apps.platform.backend.controllers.prices;

import com.inditex.apps.platform.backend.dtos.PriceHttpRequest;
import com.inditex.apps.platform.backend.dtos.PricesHttpResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class PricesHttpRestController {
    private static final Logger logger = LoggerFactory.getLogger(PricesHttpRestController.class);
    private final PricesHttpRestGetHandler pricesHttpGetHandler;

    @Autowired
    public PricesHttpRestController(PricesHttpRestGetHandler pricesHttpGetHandler) {
        this.pricesHttpGetHandler = pricesHttpGetHandler;
    }


    @GetMapping
    public ResponseEntity<PricesHttpResponse> getProductPriceOfBrandOnDated(@Valid @ModelAttribute PriceHttpRequest priceRequest) {
        logger.info("PricesHttpRestController. new get request.");
        return pricesHttpGetHandler.run(priceRequest);
    }


}
