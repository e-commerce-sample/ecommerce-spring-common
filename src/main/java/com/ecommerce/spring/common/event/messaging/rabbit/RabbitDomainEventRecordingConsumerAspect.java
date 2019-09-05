package com.ecommerce.spring.common.event.messaging.rabbit;

import com.ecommerce.spring.common.event.DomainEventConsumingTransactionAdapter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RabbitDomainEventRecordingConsumerAspect {
    private DomainEventConsumingTransactionAdapter domainEventConsumingTransactionAdapter;

    public RabbitDomainEventRecordingConsumerAspect(DomainEventConsumingTransactionAdapter domainEventConsumingTransactionAdapter) {
        this.domainEventConsumingTransactionAdapter = domainEventConsumingTransactionAdapter;
    }

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitHandler) ||" +
            " @annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object recordEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        return domainEventConsumingTransactionAdapter.recordAndConsume(joinPoint);
    }
}