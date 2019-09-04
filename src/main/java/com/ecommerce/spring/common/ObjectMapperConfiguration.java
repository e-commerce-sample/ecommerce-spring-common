package com.ecommerce.spring.common;

import com.ecommerce.shared.jackson.DefaultObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public DefaultObjectMapper objectMapper() {
        return new DefaultObjectMapper();
    }

}
