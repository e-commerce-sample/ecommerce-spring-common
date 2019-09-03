package com.ecommerce.common.event.inventory;

import com.ecommerce.sdk.base.DomainEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InventoryEvent extends DomainEvent {
    private String productId;


    protected InventoryEvent(String productId) {
        super();
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
