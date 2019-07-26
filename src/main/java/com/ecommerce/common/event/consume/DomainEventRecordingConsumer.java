package com.ecommerce.common.event.consume;

import com.ecommerce.common.event.DomainEvent;
import com.ecommerce.common.logging.AutoNamingLoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DomainEventRecordingConsumer {
    private static final Logger logger = AutoNamingLoggerFactory.getLogger();

    private DomainEventRecordDao dao;

    public DomainEventRecordingConsumer(DomainEventRecordDao dao) {
        this.dao = dao;
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
                dao.recordEvent(event);
            } catch (DuplicateKeyException dke) {
                logger.warn("Duplicated {} skipped.", event);
                return null;
            }

            return joinPoint.proceed();
        }

        return joinPoint.proceed();
    }

}
