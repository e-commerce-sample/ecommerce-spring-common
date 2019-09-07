package com.ecommerce.spring.common.event.messaging.rabbit;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class RabbitDomainEventSender implements DomainEventSender {
    private final RabbitTemplate rabbitTemplate;
    private final EcommerceRabbitProperties ecommerceRabbitProperties;


    public RabbitDomainEventSender(MessageConverter messageConverter,
                                   EcommerceRabbitProperties ecommerceRabbitProperties,
                                   RabbitTemplate rabbitTemplate) {
        this.ecommerceRabbitProperties = ecommerceRabbitProperties;
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setChannelTransacted(true);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional(transactionManager = "rabbitTransactionManager")
    public void send(DomainEvent event) {
        String exchange = ecommerceRabbitProperties.getPublishX();
        String routingKey = event.getClass().getName();
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.debug("Send {}.", event);
    }

}
