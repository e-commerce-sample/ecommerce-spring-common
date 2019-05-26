package com.ecommerce.common.event.order;

import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public abstract class OrderEvent extends DomainEvent {
    private final String orderId;

    @JsonCreator
    protected OrderEvent(@JsonProperty("orderId") String orderId,
                         @JsonProperty("_id") String _id,
                         @JsonProperty("_type") DomainEventType _type,
                         @JsonProperty("_createdAt") Instant _createdAt) {
        super(_id, _type, _createdAt);
        this.orderId = orderId;
    }

    protected OrderEvent(String orderId, DomainEventType eventType) {
        super(eventType);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
