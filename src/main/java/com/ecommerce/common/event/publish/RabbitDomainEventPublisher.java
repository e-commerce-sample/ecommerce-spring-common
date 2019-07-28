package com.ecommerce.common.event.publish;

import com.ecommerce.common.distributedlock.DistributedLockExecutor;
import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.event.DomainEventType;
import com.ecommerce.common.event.EcommerceRabbitProperties;
import com.ecommerce.common.logging.AutoNamingLoggerFactory;
import net.javacrumbs.shedlock.core.LockConfiguration;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class RabbitDomainEventPublisher {
    private static final Logger logger = AutoNamingLoggerFactory.getLogger();
    private final DomainEventDao eventDao;
    private final RabbitTemplate rabbitTemplate;
    private DistributedLockExecutor lockExecutor;
    private EcommerceRabbitProperties properties;


    public RabbitDomainEventPublisher(DomainEventDao eventDao,
                                      ConnectionFactory connectionFactory,
                                      MessageConverter messageConverter,
                                      DistributedLockExecutor lockExecutor,
                                      EcommerceRabbitProperties properties) {
        this.eventDao = eventDao;
        this.lockExecutor = lockExecutor;
        this.properties = properties;
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String eventId = correlationData.getId();
            if (ack) {
                eventDao.delete(eventId);
            } else {
                logger.warn("Domain event[{}] is nacked while publish:{}.", eventId, cause);
            }

        });
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish() {
        Instant now = Instant.now();
        LockConfiguration configuration = new LockConfiguration("domain-event-publisher", now.plusSeconds(10), now.plusSeconds(1));
        lockExecutor.execute(this::doPublish, configuration);
    }

    private Void doPublish() {
        List<DomainEvent> newestEvents = eventDao.toBePublishedEvents();
        newestEvents.forEach(event -> {
            try {
                String exchange = properties.getPublishX();
                DomainEventType eventType = event.get_type();
                String routingKey = eventType.toRoutingKey();
                eventDao.increasePublishTries(event.get_id());
                rabbitTemplate.convertAndSend(exchange,
                        routingKey,
                        event,
                        new CorrelationData(event.get_id()));
            } catch (Throwable t) {
                logger.error("Error while publish domain event {}:{}", event, t.getMessage());
            }
        });
        return null;
    }

}
