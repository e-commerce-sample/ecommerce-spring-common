package com.ecommerce.common.event;

import java.time.Instant;

import static com.ecommerce.common.utils.UuidGenerator.newUuid;
import static java.time.Instant.now;


public abstract class DomainEvent {
    private final String _id;
    private final String _type;
    private final Instant _createdAt;

    //When normal create, do initiation
    //When used by Jackson's deserialization, serves as default constructor, fields will be overrides in latter fields injection
    protected DomainEvent() {
        this._id = newUuid();
        this._type = this.getClass().getName();
        this._createdAt = now();
    }

    public String get_id() {
        return _id;
    }

    public String get_type() {
        return _type;
    }

    public Instant get_createdAt() {
        return _createdAt;
    }

    @Override
    public String toString() {
        return "DomainEvent{" +
                "_id='" + _id + '\'' +
                ", _createdAt=" + _createdAt +
                ", _type='" + _type + '\'' +
                '}';
    }
}
