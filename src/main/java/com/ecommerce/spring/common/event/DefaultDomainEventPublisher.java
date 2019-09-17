package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventDao;
import com.ecommerce.shared.event.DomainEventPublisher;
import com.ecommerce.shared.event.DomainEventSender;
import com.ecommerce.shared.utils.DistributedLockExecutor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.time.Instant;
import java.util.List;


@Slf4j
public class DefaultDomainEventPublisher implements DomainEventPublisher {
    private final DomainEventDao eventDao;
    private final DistributedLockExecutor lockExecutor;
    private final DomainEventSender sender;
    private final RetryTemplate retryTemplate;

    public DefaultDomainEventPublisher(DomainEventDao eventDao,
                                       DistributedLockExecutor lockExecutor,
                                       DomainEventSender sender) {
        this.eventDao = eventDao;
        this.lockExecutor = lockExecutor;
        this.sender = sender;
        this.retryTemplate = retryTemplate();
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(200);
        policy.setMaxInterval(2000);
        policy.setMultiplier(2.0);
        retryTemplate.setBackOffPolicy(policy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Override
    public void publishNextBatch() {
        publishNextBatch(100);
    }

    @Override
    public void publishNextBatch(int size) {
        try {
            retryTemplate.execute(context -> {
                Instant now = Instant.now();
                LockConfiguration configuration = new LockConfiguration("default-domain-event-publisher", now.plusSeconds(60));
                lockExecutor.execute(() -> doPublish(size), configuration);
                return null;
            });
        } catch (Throwable e) {
            log.warn("Error while publish domain events:{}", e.getMessage());
        }
    }


    private Void doPublish(int size) {
        List<DomainEvent> newestEvents = eventDao.nextPublishBatch(size);
        newestEvents.forEach(event -> {
            try {
                sender.send(event);
                log.info("Published {}.", event);
                eventDao.markAsPublished(event.get_id());
            } catch (Throwable t) {
                log.error("Error while publish domain event {}.", event, t);
                eventDao.markAsPublishFailed(event.get_id());
            }
        });
        return null;
    }

    @Override
    public void forcePublish(String eventId) {
        try {
            DomainEvent event = eventDao.get(eventId);
            sender.send(event);
            eventDao.markAsPublished(eventId);
        } catch (Throwable t) {
            eventDao.markAsPublishFailed(eventId);
            log.error("Error while force publish domain event {}.", eventId, t);
        }
    }
}


