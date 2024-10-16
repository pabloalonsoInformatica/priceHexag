package com.inditex.contexts.platform.shared.shared.application.services;

import com.inditex.contexts.shared.domain.AggregateRoot;

public interface ServiceSpecificationSearch <T extends AggregateRoot<T>> {

    public ServiceSpecificationSearchResponse<T> run( Class<T> type, ServiceSpecificationSearchQuery query);
}
