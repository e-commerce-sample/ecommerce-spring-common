package com.ecommerce.common.event.inventory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryChangedEvent extends InventoryEvent {
    private int remains;


    public InventoryChangedEvent(String productId, int remains) {
        super(productId);
        this.remains = remains;
    }

    public int getRemains() {
        return remains;
    }
}
