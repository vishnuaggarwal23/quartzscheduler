package com.vishnu.aggarwal.admin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class Admin {
    public static void main(String[] args) {
        SpringApplication.run(Admin.class, args);
    }
}
