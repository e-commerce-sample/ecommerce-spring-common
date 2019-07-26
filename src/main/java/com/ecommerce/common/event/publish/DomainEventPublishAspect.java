package com.ecommerce.common.event.publish;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DomainEventPublishAspect {
    private TaskExecutor taskExecutor;
    private RabbitDomainEventPublisher publisher;

    public DomainEventPublishAspect(TaskExecutor taskExecutor,
                                    RabbitDomainEventPublisher publisher) {
        this.taskExecutor = taskExecutor;
        this.publisher = publisher;
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void publishEvents(JoinPoint joinPoint) {
        taskExecutor.execute(() -> publisher.publish());
    }
}