package com.ecommerce.common.event.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductChangedEvent extends OrderEvent {
    private String productId;
    private int originCount;
    private int newCount;


    public OrderProductChangedEvent(String orderId, String productId, int originCount, int newCount) {
        super(orderId);
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
