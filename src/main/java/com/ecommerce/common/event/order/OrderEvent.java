package com.ecommerce.common.event.order;

import com.ecommerce.common.event.DomainEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OrderEvent extends DomainEvent {
    private String orderId;

    protected OrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
