package com.ecommerce.spring.common.event.messaging.kafka;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@EnableConfigurationProperties(EcommerceKafkaProperties.class)
public class KafkaConfiguration {

}
