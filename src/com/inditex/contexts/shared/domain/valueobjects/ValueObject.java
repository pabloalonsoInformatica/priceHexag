package com.inditex.contexts.shared.domain.valueobjects;

import com.inditex.contexts.shared.domain.AppError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class ValueObject <T>{
    protected T value;
    private static final Set<Class<?>> PRIMITIVES = Set.of(
            String.class,
            Integer.class,
            Long.class,
            BigDecimal.class,
            Boolean.class,
            LocalDateTime.class
    );

    public T value() {
        return value;
    }

    public ValueObject(T value) {
        this.ensureNotNull(value);
        this.ensurePrimitiveDataType(value);
        this.value = value;
    }

    private void ensurePrimitiveDataType (T value){
        if (!PRIMITIVES.contains(value.getClass())){
            throw new AppError("unsupported data type for valueobject");
        }
    }

    private void ensureNotNull(T value){
        Objects.requireNonNull(value, "The valueobject cannot be null");
    }

}
