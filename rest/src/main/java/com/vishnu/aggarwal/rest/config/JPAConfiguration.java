package com.vishnu.aggarwal.rest.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Jpa configuration.
 */
@Configuration("jpaConfiguration")
@EntityScan("com.vishnu.aggarwal.rest.entity")
@EnableJpaRepositories("com.vishnu.aggarwal.rest.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JPAConfiguration {

    @Bean(value = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
