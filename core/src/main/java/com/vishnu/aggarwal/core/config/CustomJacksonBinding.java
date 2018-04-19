package com.vishnu.aggarwal.core.config;

/*
Created by vishnu on 8/3/18 10:55 AM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The type Custom jackson binding.
 */
@Configuration
public class CustomJacksonBinding {

    /**
     * Object mapper object mapper.
     *
     * @return the object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
