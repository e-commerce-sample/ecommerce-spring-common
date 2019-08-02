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
    private DomainEventPublisher publisher;

    public DomainEventPublishAspect(TaskExecutor taskExecutor,
                                    DomainEventPublisher publisher) {
        this.taskExecutor = taskExecutor;
        this.publisher = publisher;
    }

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.amqp.rabbit.annotation.RabbitHandler) ||" +
            "@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener) ||" +
            "@annotation(com.ecommerce.common.event.consume.EcommerceRabbitListener)")
    public void publishEvents(JoinPoint joinPoint) {
        logger.debug("Trigger domain event publish process.");
        taskExecutor.execute(() -> publisher.publish());
    }
}