package com.inditex.contexts.platform.shared.shared.domain.valueobjects;

import java.util.Map;

public class SpecificationFilter {
    SpecificationField field;
    // SpecificationFilterOperator operator;  It is not necessary yet
    SpecificationFilterValue value;

    public SpecificationFilter(SpecificationField field, SpecificationFilterValue value) {
        this.field = field;
        this.value = value;
    }

    public SpecificationField getField() {
        return field;
    }

    public SpecificationFilterValue getValue() {
        return value;
    }

    public Map<String, String> toPrimitives() {
        return Map.of(
                "field", field.value(),
                "value", value.value()
        );
    }

    public static SpecificationFilter fromPrimitives(Map<String, String> data) {
        return new SpecificationFilter(
                new SpecificationField(data.get("field")),
                new SpecificationFilterValue(data.get("value"))
        );
    }

}
