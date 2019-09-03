package com.ecommerce.spring.common.event.consume;

import com.ecommerce.shared.event.consume.DomainEventConsumingWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.annotation.Transactional;

@Aspect
public class RabbitDomainEventRecordingConsumerAspect {
    private DomainEventConsumingWrapper eventConsumingWrapper;

    public RabbitDomainEventRecordingConsumerAspect(DomainEventConsumingWrapper eventConsumingWrapper) {
        this.eventConsumingWrapper = eventConsumingWrapper;
    }

    @Transactional
    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitHandler) ||" +
            " @annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object recordEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventConsumingWrapper.recordAndConsume(joinPoint);
    }
}