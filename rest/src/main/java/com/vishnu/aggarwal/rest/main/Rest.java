package com.vishnu.aggarwal.rest.main;

import com.vishnu.aggarwal.core.CoreModule;
import com.vishnu.aggarwal.rest.RestModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
public class Rest {
    public static void main(String[] args) {
        SpringApplication.run(Rest.class, args);
    }
}
