package com.ecommerce.common.event.order;

import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import static com.ecommerce.common.event.DomainEventType.ORDER_PRODUCT_CHANGED;

public class OrderProductChangedEvent extends OrderEvent {
    private final String productId;
    private final int originCount;
    private final int newCount;

    @JsonCreator
    private OrderProductChangedEvent(@JsonProperty("_id") String _id,
                                    @JsonProperty("_type") DomainEventType _type,
                                    @JsonProperty("_createdAt") Instant _createdAt,
                                    @JsonProperty("orderId") String orderId,
                                    @JsonProperty("productId") String productId,
                                    @JsonProperty("originCount") int originCount,
                                    @JsonProperty("newCount") int newCount) {
        super(orderId, _id, _type, _createdAt);
        this.productId = productId;
        this.originCount = originCount;
        this.newCount = newCount;
    }

    public OrderProductChangedEvent(String orderId, String productId, int originCount, int newCount) {
        super(orderId, ORDER_PRODUCT_CHANGED);
        this.productId = productId;
        this.originCount = originCount;
        this.newCount = newCount;
    }

    public String getProductId() {
        return productId;
    }

    public int getOriginCount() {
        return originCount;
    }

    public int getNewCount() {
        return newCount;
    }
}
