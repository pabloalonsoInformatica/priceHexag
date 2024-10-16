package com.inditex.contexts.platform.shared.shared.domain;

import com.inditex.contexts.platform.shared.shared.domain.valueobjects.SpecificationFilter;
import com.inditex.contexts.platform.shared.shared.domain.valueobjects.SpecificationFilterValue;
import com.inditex.contexts.shared.domain.AggregateRoot;

import java.util.*;
import java.util.stream.Collectors;

public class Specification extends AggregateRoot<Specification> {
    Set<SpecificationFilter> filters;

    public Specification(Builder builder) {
        this.filters = builder.filters;
    }

    public static class Builder {
        private Set<SpecificationFilter> filters = new LinkedHashSet<>();

        public Builder filters(Set<SpecificationFilter> filters) {
            if (filters != null) {
                this.filters.addAll(filters);
            }
            return this;
        }

        public Specification build() {
            return new Specification(this);
        }
    }

    public Set<SpecificationFilter> getFilters() {
        return filters;
    }

    @Override
    public Map<String, Object> toPrimitives() {
        return Map.of(
                "filters", filters.stream()
                        .map(SpecificationFilter::toPrimitives)
                        .toList()
        );
    }

    public static Specification fromPrimitives(Map<String, Object> data) {
        @SuppressWarnings("unchecked")
        Set<SpecificationFilter> filters = ((List<Map<String, String>>) data.get("filters")).stream()
                .map(SpecificationFilter::fromPrimitives)
                .collect(Collectors.toSet());

        return new Specification.Builder().filters(filters).build();
    }

    public Optional<SpecificationFilter> getFilterOfFilterField (String field) {
        return this.filters.stream()
                .filter(filter -> field.equals(filter.getField().value()))
                .findFirst();
    }

    public Optional<SpecificationFilterValue> getValueFilterOfFilterField (String field) {
        return this.getFilterOfFilterField(field)
                .map(SpecificationFilter::getValue);
    }
}
