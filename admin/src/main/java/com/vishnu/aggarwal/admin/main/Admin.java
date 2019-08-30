package com.vishnu.aggarwal.admin.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.SpringProperties;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
public class Admin {
    public static void main(String[] args) {
        SpringProperties.setProperty("spring.devtools.restart.enabled", "true");
        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringProperties.setProperty("spring.thymeleaf.cache", "true");
        System.setProperty("spring.thymeleaf.cache", "true");
        SpringProperties.setProperty("spring.devtools.livereload.enabled", "true");
        System.setProperty("spring.devtools.livereload.enabled", "true");
//        SpringProperties.setProperty("spring.devtools.livereload.port", "35730");
//        System.setProperty("spring.devtools.livereload.port", "35730");
        run(Admin.class, args);
    }
}
