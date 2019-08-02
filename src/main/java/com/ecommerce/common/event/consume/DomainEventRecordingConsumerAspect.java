package com.ecommerce.common.event.consume;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DomainEventRecordingConsumerAspect {
    private DomainEventRecordingConsumer domainEventRecordingConsumer;

    public DomainEventRecordingConsumerAspect(DomainEventRecordingConsumer domainEventRecordingConsumer) {
        this.domainEventRecordingConsumer = domainEventRecordingConsumer;
    }

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitHandler) ||" +
            " @annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object recordEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        return domainEventRecordingConsumer.recordAndConsume(joinPoint);
    }
}