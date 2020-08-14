package wrapper.quartz.scheduler.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Mongo configuration.
 */
@Configuration
@EntityScan("wrapper.quartz.scheduler.entity.mongo")
@EnableMongoRepositories("wrapper.quartz.scheduler.repository.mongo")
@EnableTransactionManagement
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class MongoConfiguration {
}
