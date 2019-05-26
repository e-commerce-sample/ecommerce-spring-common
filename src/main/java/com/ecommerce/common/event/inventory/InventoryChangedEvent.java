package com.ecommerce.common.event.inventory;

import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import static com.ecommerce.common.event.DomainEventType.INVENTORY_CHANGED;

public class InventoryChangedEvent extends InventoryEvent {
    private final int remains;

    @JsonCreator
    private InventoryChangedEvent(@JsonProperty("_id") String _id,
                                 @JsonProperty("_type") DomainEventType _type,
                                 @JsonProperty("_createdAt") Instant _createdAt,
                                 @JsonProperty("productId") String productId,
                                 @JsonProperty("remains") int remains) {
        super(productId, _id, _type, _createdAt);
        this.remains = remains;
    }

    public InventoryChangedEvent(String productId, int remains) {
        super(productId, INVENTORY_CHANGED);
        this.remains = remains;
    }

    public int getRemains() {
        return remains;
    }
}
