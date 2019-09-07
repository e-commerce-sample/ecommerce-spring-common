package com.ecommerce.spring.common.event.messaging.rabbit;

import com.ecommerce.shared.event.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Aspect
public class RabbitDomainEventPublishAspect {
    private TaskExecutor taskExecutor;
    private DomainEventPublisher publisher;

    public RabbitDomainEventPublishAspect(DomainEventPublisher publisher) {
        this.taskExecutor = taskExecutor();
        this.publisher = publisher;
    }

    private TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setRejectedExecutionHandler((r, e)
                -> log.debug("Domain event publish job rejected silently."));
        executor.setThreadNamePrefix("domain-event-publish-executor-");
        executor.initialize();
        return executor;
    }

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.amqp.rabbit.annotation.RabbitHandler) ||" +
            "@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener) ||" +
            "@annotation(com.ecommerce.spring.common.event.messaging.rabbit.EcommerceRabbitListener)")
    public void publishEvents(JoinPoint joinPoint) {
        log.info("Trigger domain event publish process using Spring AOP.");
        taskExecutor.execute(() -> publisher.publishNextBatch());
    }
}