package com.ecommerce.spring.common.event.recording.mongo;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventDao;

import java.util.List;

public class MongoDomainEventDao implements DomainEventDao {

    @Override
    public void save(List<DomainEvent> events) {

    }

    @Override
    public void delete(String eventId) {

    }

    @Override
    public DomainEvent get(String eventId) {
        return null;
    }

    @Override
    public List<DomainEvent> nextPublishBatch(int size) {
        return null;
    }

    @Override
    public void markAsPublished(String eventId) {

    }

    @Override
    public void markAsPublishFailed(String eventId) {

    }

    @Override
    public void deleteAll() {

    }
}
