package com.ecommerce.common.event.publish;

import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.event.DomainEventType;
import com.ecommerce.common.event.EcommerceRabbitProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RabbitDomainEventSender {
    private final RabbitTemplate rabbitTemplate;
    private final EcommerceRabbitProperties properties;


    public RabbitDomainEventSender(ConnectionFactory connectionFactory,
                                   MessageConverter messageConverter,
                                   EcommerceRabbitProperties properties) {
        this.properties = properties;
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setChannelTransacted(true);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional(transactionManager = "rabbitTransactionManager")
    public void send(DomainEvent event) {
        String exchange = properties.getPublishX();
        DomainEventType eventType = event.get_type();
        String routingKey = eventType.toRoutingKey();
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
