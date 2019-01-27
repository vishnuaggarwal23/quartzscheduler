package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 23/5/18 3:23 PM
*/

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration(value = "mongoConfiguration")
@EntityScan("com.vishnu.aggarwal.rest.document")
@EnableMongoRepositories("com.vishnu.aggarwal.rest.repository.mongo")
@EnableTransactionManagement
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class MongoConfiguration {
}
