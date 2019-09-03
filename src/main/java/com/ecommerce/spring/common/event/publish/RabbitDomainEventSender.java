package com.ecommerce.spring.common.event.publish;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.spring.common.event.EcommerceRabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RabbitDomainEventSender {
    private final RabbitTemplate rabbitTemplate;
    private final EcommerceRabbitProperties properties;


    public RabbitDomainEventSender(MessageConverter messageConverter,
                                   EcommerceRabbitProperties properties,
                                   RabbitTemplate rabbitTemplate) {
        this.properties = properties;
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setChannelTransacted(true);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional(transactionManager = "rabbitTransactionManager")
    public void send(DomainEvent event) {
        String exchange = properties.getPublishX();
        String routingKey = event.getClass().getName();
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
