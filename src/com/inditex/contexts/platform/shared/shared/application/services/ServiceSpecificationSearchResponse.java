package com.inditex.contexts.platform.shared.shared.application.services;

import java.util.Arrays;
import java.util.Objects;

public class ServiceSpecificationSearchResponse <T> {
    T[] data;
    Long count;

    public ServiceSpecificationSearchResponse(T[] data, Long count) {
        this.data = data;
        this.count = count;
    }

    public T[] getData() {
        return data;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceSpecificationSearchResponse<?> that = (ServiceSpecificationSearchResponse<?>) o;
        return Arrays.equals(data, that.data) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(data), count);
    }
}
