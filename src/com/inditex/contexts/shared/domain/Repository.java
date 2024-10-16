package com.inditex.contexts.shared.domain;

import com.inditex.contexts.platform.shared.shared.domain.Specification;

import java.util.Optional;

public interface Repository <T extends AggregateRoot<T>> {
    public Optional<T> matchingOneDocument (Specification specification);
}
