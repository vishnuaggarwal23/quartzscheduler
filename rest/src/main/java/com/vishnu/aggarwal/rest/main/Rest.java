package com.vishnu.aggarwal.rest.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
public class Rest {
    public static void main(String[] args) {
        SpringApplication.run(Rest.class, args);
    }
}
