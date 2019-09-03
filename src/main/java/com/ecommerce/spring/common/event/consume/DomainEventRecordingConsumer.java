package com.ecommerce.spring.common.event.consume;

import com.ecommerce.shared.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class DomainEventRecordingConsumer {

    private JdbcTemplateDomainEventRecorder recorder;

    public DomainEventRecordingConsumer(JdbcTemplateDomainEventRecorder recorder) {
        this.recorder = recorder;
    }

    @Transactional
    public Object recordAndConsume(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Optional<Object> optionalEvent = Arrays.stream(args)
                .filter(o -> o instanceof DomainEvent)
                .findFirst();

        if (optionalEvent.isPresent()) {
            DomainEvent event = (DomainEvent) optionalEvent.get();
            try {
                recorder.record(event);
            } catch (DuplicateKeyException dke) {
                log.warn("Duplicated {} skipped.", event);
                return null;
            }

            return joinPoint.proceed();
        }

        return joinPoint.proceed();
    }

}
