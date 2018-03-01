package com.vishnu.aggarwal.rest.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The type Rest.
 */
@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
public class Rest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Rest.class, args);
    }
}
