package com.ecommerce.common.event.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    private final String productId;
    private final int count;

    @JsonCreator
    public OrderItem(@JsonProperty("productId") String productId,
                     @JsonProperty("count") int count) {
        this.productId = productId;
        this.count = count;
    }

    public String getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }
}
