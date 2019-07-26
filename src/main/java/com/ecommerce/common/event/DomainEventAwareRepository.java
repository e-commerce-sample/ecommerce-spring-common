package com.ecommerce.common.event;

import com.ecommerce.common.event.publish.DomainEventDao;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DomainEventAwareRepository<AR extends DomainEventAwareAggregate> {
    @Autowired
    private DomainEventDao eventDao;

    public void save(AR aggregate) {
        eventDao.insert(aggregate.getEvents());
        aggregate.clearEvents();
        doSave(aggregate);
    }

    protected abstract void doSave(AR aggregate);
}
