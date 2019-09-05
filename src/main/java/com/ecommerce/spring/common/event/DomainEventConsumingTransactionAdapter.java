package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.DomainEventConsumingWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.annotation.Transactional;

public class DomainEventConsumingTransactionAdapter {
    private DomainEventConsumingWrapper eventConsumingWrapper;

    public DomainEventConsumingTransactionAdapter(DomainEventConsumingWrapper wrapper) {
        this.eventConsumingWrapper = wrapper;
    }

    @Transactional
    public Object recordAndConsume(ProceedingJoinPoint joinPoint) throws Throwable {
        return eventConsumingWrapper.recordAndConsume(joinPoint);
    }
}
