package com.ecommerce.common.event.product;

import com.ecommerce.common.event.DomainEventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import static com.ecommerce.common.event.DomainEventType.PRODUCT_NAME_UPDATED;

public class ProductNameUpdatedEvent extends ProductEvent {
    private String oldName;
    private String newName;

    @JsonCreator
    private ProductNameUpdatedEvent(@JsonProperty("_id") String _id,
                                    @JsonProperty("_type") DomainEventType _type,
                                    @JsonProperty("_createdAt") Instant _createdAt,
                                    @JsonProperty("productId") String productId,
                                    @JsonProperty("oldName") String oldName,
                                    @JsonProperty("newName") String newName) {
        super(productId, _id, _type, _createdAt);
        this.oldName = oldName;
        this.newName = newName;
    }

    public ProductNameUpdatedEvent(String productId, String oldName, String newName) {
        super(productId, PRODUCT_NAME_UPDATED);
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}
