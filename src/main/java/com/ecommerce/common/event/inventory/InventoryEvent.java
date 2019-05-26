package com.ecommerce.common.event.inventory;

import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public abstract class InventoryEvent extends DomainEvent {
    private final String productId;

    @JsonCreator
    protected InventoryEvent(@JsonProperty("productId") String productId,
                             @JsonProperty("_id") String _id,
                             @JsonProperty("_type") DomainEventType _type,
                             @JsonProperty("_createdAt") Instant _createdAt) {
        super(_id, _type, _createdAt);
        this.productId = productId;
    }

    protected InventoryEvent(String productId, DomainEventType _type) {
        super(_type);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
