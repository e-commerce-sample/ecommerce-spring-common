package com.ecommerce.spring.common.event.messaging.rabbit;

import com.ecommerce.shared.event.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Aspect
public class RabbitDomainEventPublishAspect {

    private TaskExecutor taskExecutor;
    private DomainEventPublisher publisher;

    public RabbitDomainEventPublishAspect(TaskExecutor taskExecutor,
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
            "@annotation(com.ecommerce.spring.common.event.messaging.rabbit.EcommerceRabbitListener)")
    public void publishEvents(JoinPoint joinPoint) {
        log.debug("Trigger domain event publish process.");
        taskExecutor.execute(() -> publisher.publish());
    }
}