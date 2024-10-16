package com.inditex.contexts.platform.prices.infrastructure.persistence.h2;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class JpaPriceSpecification {

    private JpaPriceSpecification() {
        // utility class
    }
    public static Specification<PriceDocument> onDate (LocalDateTime applicationDate){
        return (root, query, criteriaBuilder) -> {
            if (applicationDate == null) {
                return criteriaBuilder.conjunction(); // no filter applies
            }
            return criteriaBuilder.between(criteriaBuilder.literal(applicationDate), root.get("startDate"), root.get("endDate"));
        };
    }

    public static Specification<PriceDocument> brandIs(Long brand) {
        return (root, query, criteriaBuilder) -> {
            if (brand == null) {
                return criteriaBuilder.conjunction(); //  no filter applies
            }
            return criteriaBuilder.equal(root.get("brand"), brand);
        };
    }

    public static Specification<PriceDocument> productIs(Long product) {
        return (root, query, criteriaBuilder) -> {
            if (product == null) {
                return criteriaBuilder.conjunction(); //  no filter applies
            }
            return criteriaBuilder.equal(root.get("product"), product);
        };
    }
}
