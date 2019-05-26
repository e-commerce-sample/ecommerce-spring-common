package com.ecommerce.common.event.order;

import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import static com.ecommerce.common.event.DomainEventType.ORDER_PAID;

public class OrderPaidEvent extends OrderEvent {
    @JsonCreator
    private OrderPaidEvent(@JsonProperty("_id") String _id,
                           @JsonProperty("_type") DomainEventType _type,
                           @JsonProperty("_createdAt") Instant _createdAt,
                           @JsonProperty("orderId") String orderId) {
        super(orderId, _id, _type, _createdAt);
    }

    public OrderPaidEvent(String orderId) {
        super(orderId, ORDER_PAID);
    }

}
