package com.inditex.contexts.platform.shared.shared.application.services;

import java.util.Set;

public class ServiceSpecificationSearchQuery {
    Set<ServiceSpecificationSearchQueryFilter> filters;

    public ServiceSpecificationSearchQuery(Set<ServiceSpecificationSearchQueryFilter> filters) {
        this.filters = filters;
    }
}
