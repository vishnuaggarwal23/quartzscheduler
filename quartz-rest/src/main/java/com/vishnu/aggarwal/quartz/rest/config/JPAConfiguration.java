package com.vishnu.aggarwal.quartz.rest.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Jpa configuration.
 */
@Configuration("jpaConfiguration")
@EntityScan("com.vishnu.aggarwal.quartz.rest.entity")
@EnableJpaRepositories("com.vishnu.aggarwal.quartz.rest.repository.jpa")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class JPAConfiguration {
}
