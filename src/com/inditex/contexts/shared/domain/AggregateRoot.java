package com.inditex.contexts.shared.domain;

import java.util.Map;

public abstract class AggregateRoot <T>{
    public abstract Map<String, Object> toPrimitives();
}
