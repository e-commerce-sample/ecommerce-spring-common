package com.ecommerce.common.event.product;

import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public abstract class ProductEvent extends DomainEvent {
    private final String productId;

    @JsonCreator
    protected ProductEvent(@JsonProperty("productId") String productId,
                           @JsonProperty("_id") String _id,
                           @JsonProperty("_type") DomainEventType _type,
                           @JsonProperty("_createdAt") Instant _createdAt) {
        super(_id, _type, _createdAt);
        this.productId = productId;
    }

    public ProductEvent(String productId, DomainEventType eventType) {
        super(eventType);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
