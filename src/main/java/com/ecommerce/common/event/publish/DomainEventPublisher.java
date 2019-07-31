package com.ecommerce.common.event.publish;

import com.ecommerce.common.distributedlock.DistributedLockExecutor;
import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.logging.AutoNamingLoggerFactory;
import net.javacrumbs.shedlock.core.LockConfiguration;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DomainEventPublisher {
    private static final Logger logger = AutoNamingLoggerFactory.getLogger();
    private final DomainEventDao eventDao;
    private DistributedLockExecutor lockExecutor;
    private RabbitDomainEventSender eventSender;


    public DomainEventPublisher(DomainEventDao eventDao,
                                DistributedLockExecutor lockExecutor,
                                RabbitDomainEventSender eventSender) {
        this.eventDao = eventDao;
        this.lockExecutor = lockExecutor;
        this.eventSender = eventSender;

    }

    public void publish() {
        Instant now = Instant.now();
        LockConfiguration configuration = new LockConfiguration("domain-event-publisher", now.plusSeconds(30));
        lockExecutor.execute(this::doPublish, configuration);
    }

    private Void doPublish() {
        List<DomainEvent> newestEvents = eventDao.toBePublishedEvents();
        newestEvents.forEach(event -> {
            try {
                eventDao.increasePublishTries(event.get_id());
                eventSender.send(event);
                eventDao.delete(event.get_id());
            } catch (Throwable t) {
                logger.error("Error while publish domain event {}:{}", event, t.getMessage());
            }
        });
        return null;
    }

}
