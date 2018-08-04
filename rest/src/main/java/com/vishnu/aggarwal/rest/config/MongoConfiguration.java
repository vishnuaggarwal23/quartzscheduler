package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 23/5/18 3:23 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration(value = "mongoConfiguration")
@EntityScan("com.vishnu.aggarwal.rest.document")
@EnableMongoRepositories("com.vishnu.aggarwal.rest.repository.mongo")
@EnableTransactionManagement
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class MongoConfiguration {

    private final UserService userService;

    public MongoConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean(value = "auditorAware")
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl(userService);
    }
}
