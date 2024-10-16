package com.inditex.contexts.platform.prices.domain;

import com.inditex.contexts.platform.prices.domain.valueobjects.*;
import com.inditex.contexts.shared.domain.AggregateRoot;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class Price extends AggregateRoot<Price> {

    PriceId id;
    PriceStartDate startDate;
    PriceEndDate endDate;
    PriceCurrency currency;
    PricePriority priority;
    PriceBrandId brandId;
    PriceProductId productId;
    PriceValue value;

    public Price(
            PriceId id,
            PriceStartDate startDate,
            PriceEndDate endDate,
            PriceCurrency currency,
            PricePriority priority,
            PriceBrandId brandId,
            PriceProductId productId,
            PriceValue value
    ) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currency = currency;
        this.priority = priority;
        this.brandId = brandId;
        this.productId = productId;
        this.value = value;
    }

    @Override
    public Map<String, Object> toPrimitives() {
        return Map.of(
            "id", id != null ? id.value() : null,
            "startDate", startDate != null ? startDate.toString() : null,
            "endDate", endDate != null ? endDate.toString() : null,
            "currency", currency != null ? currency.value() : null,
            "priority", priority != null ? priority.value() : null,
            "brandId", brandId != null ? brandId.value() : null,
            "productId", productId != null ? productId.value() : null,
            "value", value != null ? value.value() : null
        );
    }

    public static Price fromPrimitives(Map<String, Object> data) {
        PriceId id = new PriceId((Long) data.get("id"));
        PriceStartDate startDate = new PriceStartDate((String) data.get("startDate"));
        PriceEndDate endDate = new PriceEndDate((String) data.get("endDate"));
        PriceCurrency currency = new PriceCurrency((String) data.get("currency"));
        PricePriority priority = new PricePriority((int) data.get("priority"));
        PriceBrandId brandId = new PriceBrandId((Long) data.get("brandId"));
        PriceProductId productId = new PriceProductId((Long) data.get("productId"));
        PriceValue value = new PriceValue((BigDecimal) data.get("value"));

        return new Price(id, startDate, endDate, currency, priority, brandId, productId, value);
    }

    public static class Builder {
        private PriceId id;
        private PriceStartDate startDate;
        private PriceEndDate endDate;
        private PriceCurrency currency;
        private PricePriority priority;
        private PriceBrandId brandId;
        private PriceProductId productId;
        private PriceValue value;

        public Builder() {
            //default constructor
        }

        public Builder id(PriceId id) {
            this.id = id;
            return this;
        }

        public Builder startDate(PriceStartDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(PriceEndDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder currency(PriceCurrency currency) {
            this.currency = currency;
            return this;
        }

        public Builder priority(PricePriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder brandId(PriceBrandId brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder productId(PriceProductId productId) {
            this.productId = productId;
            return this;
        }

        public Builder value(PriceValue value) {
            this.value = value;
            return this;
        }

        public Price build() {
            return new Price(id, startDate, endDate, currency, priority, brandId, productId, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(id.value(), price.id.value()) &&
                Objects.equals(startDate.value(), price.startDate.value()) &&
                Objects.equals(endDate.value(), price.endDate.value()) &&
                Objects.equals(currency.value(), price.currency.value()) &&
                Objects.equals(priority.value(), price.priority.value()) &&
                Objects.equals(brandId.value(), price.brandId.value()) &&
                Objects.equals(productId.value(), price.productId.value()) &&
                Objects.equals(value.value(), price.value.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id != null ? id.value() : null,
                startDate != null ? startDate.value() : null,
                endDate != null ? endDate.value() : null,
                currency != null ? currency.value() : null,
                priority != null ? priority.value() : null,
                brandId != null ? brandId.value() : null,
                productId != null ? productId.value() : null,
                value != null ? value.value() : null
        );
    }

}
