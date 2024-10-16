package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchQueryFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class PricesBySpecificationSearchQueryMother {

    static PricesBySpecificationSearchQuery createDefault () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return new PricesBySpecificationSearchQuery(Set.of(
                new ServiceSpecificationSearchQueryFilter("product", "1"),
                new ServiceSpecificationSearchQueryFilter("brand", "1"),
                new ServiceSpecificationSearchQueryFilter("date", LocalDateTime.now().format(formatter))
        ));
    }

}
