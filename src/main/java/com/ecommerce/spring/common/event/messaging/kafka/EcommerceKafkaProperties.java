package com.ecommerce.spring.common.event.messaging.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties("ecommerce.kafka")
@Validated
public class EcommerceKafkaProperties {


}
