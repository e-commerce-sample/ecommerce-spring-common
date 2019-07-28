package com.ecommerce.common.event.publish;

import com.ecommerce.common.logging.AutoNamingLoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DomainEventPublishAspect {
    private static final Logger logger = AutoNamingLoggerFactory.getLogger();

    private TaskExecutor taskExecutor;
    private RabbitDomainEventPublisher publisher;

    public DomainEventPublishAspect(TaskExecutor taskExecutor,
                                    RabbitDomainEventPublisher publisher) {
        this.taskExecutor = taskExecutor;
        this.publisher = publisher;
    }

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void publishEvents(JoinPoint joinPoint) {
        logger.info("Trigger domain event publish after HTTP write methods.");
        taskExecutor.execute(() -> publisher.publish());
    }
}