package com.inditex.contexts.platform.prices.infrastructure.persistence;

import com.inditex.contexts.platform.prices.domain.Price;
import com.inditex.contexts.platform.prices.domain.valueobjects.*;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.JpaPriceRepository;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.JpaPriceSpecification;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.PriceDocument;
import com.inditex.contexts.shared.domain.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@org.springframework.stereotype.Repository
public class H2PriceRepository implements Repository<Price> {
    private static final Logger logger = LoggerFactory.getLogger(H2PriceRepository.class);
    private final JpaPriceRepository jpaPriceRepository;

    @Autowired
    public H2PriceRepository(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    public Specification<PriceDocument> buildSpecification(com.inditex.contexts.platform.shared.shared.domain.Specification querySpecification) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Specification<PriceDocument> jpaSpecification = Specification.where(null);

        //get values of filters
        Long productFilterValue = querySpecification.getValueFilterOfFilterField("product")
                .map(value -> Long.parseLong(value.value()))
                .orElse(null);
        Long brandFilterValue = querySpecification.getValueFilterOfFilterField("brand")
                .map(value -> Long.parseLong(value.value()))
                .orElse(null);
        LocalDateTime dateFilterValue = querySpecification.getValueFilterOfFilterField("date")
                .map(value -> LocalDateTime.parse(value.value(), formatter)
                        .truncatedTo(ChronoUnit.SECONDS))
                .orElse(null);

        //compose query
        if (productFilterValue != null) {
            jpaSpecification = jpaSpecification.and(JpaPriceSpecification.productIs(productFilterValue));
            logger.debug("buildSpecification. Added productFilter: {}", productFilterValue);
        }
        if (brandFilterValue != null) {
            jpaSpecification = jpaSpecification.and(JpaPriceSpecification.brandIs(brandFilterValue));
            logger.debug("buildSpecification. Added brandFilter: {}", brandFilterValue);
        }
        if (dateFilterValue != null) {
            jpaSpecification = jpaSpecification.and(JpaPriceSpecification.onDate(dateFilterValue));
            logger.debug("buildSpecification. Added dateFilter: {}", dateFilterValue);
        }
        return jpaSpecification;
    }

    @Override
    public Optional<Price> matchingOneDocument(com.inditex.contexts.platform.shared.shared.domain.Specification querySpecification) {
        Specification<PriceDocument> specification = this.buildSpecification(querySpecification);
        logger.debug("matchingOneDocument.");
        return this.jpaPriceRepository.findAll(
                specification,
                PageRequest.of(
                        0, //default value
                        1, //default value
                        Sort.by(Sort.Direction.DESC, "priority") //default value
                )
        ).stream().findFirst().map(priceResult ->
                new Price.Builder()
                        .id(new PriceId(priceResult.getId()))
                        .startDate(new PriceStartDate(priceResult.getStartDate()))
                        .endDate(new PriceEndDate(priceResult.getEndDate()))
                        .currency(new PriceCurrency(priceResult.getCurr()))
                        .priority(new PricePriority(priceResult.getPriority()))
                        .brandId(new PriceBrandId(priceResult.getBrand()))
                        .productId(new PriceProductId(priceResult.getProduct()))
                        .value(new PriceValue(priceResult.getValue()))
                        .build()
        );
    }
}
