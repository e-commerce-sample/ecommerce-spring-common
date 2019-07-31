package com.ecommerce.common.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

// For every services, you must bind your own receive-q to the exchanges that you want to receive message from
@Configuration
public class CommonRabbitmqConfig {

    private EcommerceRabbitProperties properties;
    private RabbitProperties rabbitProperties;

    public CommonRabbitmqConfig(EcommerceRabbitProperties properties,
                                RabbitProperties rabbitProperties) {
        this.properties = properties;
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(rabbitProperties.getAddresses());
        factory.setUsername(rabbitProperties.getUsername());
        factory.setPassword(rabbitProperties.getPassword());
        factory.setPort(rabbitProperties.getPort());
        factory.setVirtualHost(rabbitProperties.getVirtualHost());
        return factory;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter,
                                                                               TaskExecutor taskExecutor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(rabbitProperties.getListener().getSimple().getPrefetch());
        factory.setConcurrentConsumers(rabbitProperties.getListener().getSimple().getConcurrency());
        factory.setMaxConcurrentConsumers(rabbitProperties.getListener().getSimple().getMaxConcurrency());
        factory.setMessageConverter(messageConverter);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000L);
        RetryOperationsInterceptor build = RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffPolicy(fixedBackOffPolicy)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
        factory.setAdviceChain(build);

        factory.setTaskExecutor(taskExecutor);
        return factory;
    }


    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        messageConverter.setClassMapper(classMapper());
        return messageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        return classMapper;
    }


    //"发送方Exchange"
    @Bean
    public TopicExchange publishExchange() {
        return new TopicExchange(properties.getPublishX(), true, false, ImmutableMap.of("alternate-exchange", properties.getPublishDlx()));
    }

    //"发送方DLX"，消息发送失败时传到该DLX
    @Bean
    public TopicExchange publishDlx() {
        return new TopicExchange(properties.getPublishDlx(), true, false, null);
    }

    //"发送方DLQ"，所有发到"发送DLX"的消息都将路由到该DLQ
    @Bean
    public Queue publishDlq() {
        return new Queue(properties.getPublishDlq(), true, false, false, ImmutableMap.of("x-queue-mode", "lazy"));
    }

    //"发送方DLQ"绑定到"发送方DLX"
    @Bean
    public Binding orderPublishDlqBinding() {
        return BindingBuilder.bind(publishDlq()).to(publishDlx()).with("#");
    }

    //接收方的所有消息都发送到该"接收方Queue"，即"接收方Queue"可以绑定多个"发送方Exchange"
    @Bean
    public Queue receiveQ() {
        ImmutableMap<String, Object> args = ImmutableMap.of(
                "x-dead-letter-exchange",
                properties.getReceiveDlx(),
                "x-overflow",
                "drop-head",
                "x-max-length",
                300000,
                "x-message-ttl",
                24 * 60 * 60 * 1000);
        return new Queue(properties.getReceiveQ(), true, false, false, args);
    }


    //"接收方DLX"，消息处理失败时传到该DLX
    @Bean
    public TopicExchange receiveDlx() {
        return new TopicExchange(properties.getReceiveDlx(), true, false, null);
    }


    //"接收方DLQ"，所有发到"接收DLX"的消息都将路由到该DLQ
    @Bean
    public Queue receiveDlq() {
        return new Queue(properties.getReceiveDlq(), true, false, false, ImmutableMap.of("x-queue-mode", "lazy"));
    }

    //"接收方DLQ"绑定到"接收方DLX"
    @Bean
    public Binding receiveDlqBinding() {
        return BindingBuilder.bind(receiveDlq()).to(receiveDlx()).with("#");
    }


    //"接收方恢复Exchange"，用于手动将"接收方DLQ"中的消息发到该DLX进行重试
    @Bean
    public TopicExchange receiveRecoverExchange() {
        return new TopicExchange(properties.getReceiveRecoverX(), true, false, null);
    }

    //"接收方Queue"绑定到"接收方恢复Exchange"
    @Bean
    public Binding receiveRecoverBinding() {
        return BindingBuilder.bind(receiveQ()).to(receiveRecoverExchange()).with("#");
    }

}
