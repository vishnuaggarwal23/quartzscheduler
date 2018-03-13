package com.vishnu.aggarwal.admin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The type Admin.
 */
@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
public class Admin {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Admin.class, args);
    }
}
