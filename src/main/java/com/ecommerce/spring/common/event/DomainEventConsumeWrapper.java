package com.ecommerce.spring.common.event;

import com.ecommerce.shared.event.DomainEvent;
import com.ecommerce.shared.event.DomainEventConsumeRecorder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class DomainEventConsumeWrapper {

    private DomainEventConsumeRecorder consumeRecorder;

    public DomainEventConsumeWrapper(DomainEventConsumeRecorder consumeRecorder) {
        this.consumeRecorder = consumeRecorder;
    }

    @Transactional
    public Object recordAndConsume(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Optional<Object> optionalEvent = Arrays.stream(args)
                .filter(o -> o instanceof DomainEvent)
                .findFirst();

        if (!optionalEvent.isPresent()) {
            return joinPoint.proceed();
        }

        DomainEvent event = (DomainEvent) optionalEvent.get();
        if (!consumeRecorder.record(event)) {
            log.warn("Domain event {} is already consumed, skip it.", event);
            return null;
        }

        return joinPoint.proceed();

    }
}
