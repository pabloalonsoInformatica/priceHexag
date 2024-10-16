package com.inditex.contexts.platform.shared.shared.application.services;

public class ServiceSpecificationSearchQueryFilter{
    String field;
    String value;

    public ServiceSpecificationSearchQueryFilter(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
