package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.prices.domain.Price;
import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchImpl;
import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchResponse;
import com.inditex.contexts.shared.domain.Repository;
import org.springframework.stereotype.Component;

@Component
public class PricesBySpecificationSearch {

    private Repository<Price> repository;
    private final ServiceSpecificationSearchImpl<Price> serviceSpecificationSearch;


    public PricesBySpecificationSearch(Repository<Price> repository) {
        this.repository = repository;
        this.serviceSpecificationSearch = new ServiceSpecificationSearchImpl<>(this.repository);
    }

    public PricesByServiceSpecificationSearchResponse run (PricesBySpecificationSearchQuery query){
        ServiceSpecificationSearchResponse<Price> response = this.serviceSpecificationSearch.run(Price.class, query);
        return new PricesByServiceSpecificationSearchResponse(response.getData(), response.getCount());
    }
}
