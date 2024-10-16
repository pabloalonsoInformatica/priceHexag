package com.inditex.contexts.platform.shared.shared.application.services;

import com.inditex.contexts.platform.shared.shared.domain.Specification;
import com.inditex.contexts.platform.shared.shared.domain.valueobjects.SpecificationField;
import com.inditex.contexts.platform.shared.shared.domain.valueobjects.SpecificationFilter;
import com.inditex.contexts.platform.shared.shared.domain.valueobjects.SpecificationFilterValue;
import com.inditex.contexts.shared.domain.AggregateRoot;
import com.inditex.contexts.shared.domain.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceSpecificationSearchImpl<T extends AggregateRoot<T>> implements ServiceSpecificationSearch <T> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceSpecificationSearchImpl.class);
    private Repository<T> repository;

    public ServiceSpecificationSearchImpl(Repository<T> repository) {
        this.repository = repository;
    }

    public ServiceSpecificationSearchResponse<T> run(Class type, ServiceSpecificationSearchQuery query) {
        Set<SpecificationFilter> filters = null;

        //extract filters
        if (query.filters != null) {
            filters = query.filters.stream()
                    .map(this::convertToSpecificationFilter)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        Specification specification = new Specification.Builder().filters(filters).build();

        // query
        Optional<T> result = this.repository.matchingOneDocument(specification);

        //compose response
        T[] data = (T[]) Array.newInstance(type, result.isPresent() ? 1 : 0);
        result.ifPresent(value -> data[0] = value);
        Long count = result.isPresent() ? 1L : 0L;
        logger.debug("run. repository matchingOneDocument result_count: {}", count);
        return new ServiceSpecificationSearchResponse<>(data, count);
    }

    private SpecificationFilter convertToSpecificationFilter(ServiceSpecificationSearchQueryFilter queryFilter) {
        return new SpecificationFilter(
                new SpecificationField(queryFilter.getField()),
                new SpecificationFilterValue(queryFilter.getValue())
        );
    }
}
