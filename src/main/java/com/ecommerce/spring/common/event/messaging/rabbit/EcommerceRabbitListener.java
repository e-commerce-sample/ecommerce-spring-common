package com.ecommerce.spring.common.event.messaging.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RabbitListener(queues = {"#{'${ecommerce.rabbit.receiveQ}'}"})
public @interface EcommerceRabbitListener {
}
