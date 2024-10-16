package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchQuery;
import com.inditex.contexts.platform.shared.shared.application.services.ServiceSpecificationSearchQueryFilter;

import java.util.Set;

public class PricesBySpecificationSearchQuery extends ServiceSpecificationSearchQuery {
    public PricesBySpecificationSearchQuery(Set<ServiceSpecificationSearchQueryFilter> filters) {
        super(filters);
    }
}
