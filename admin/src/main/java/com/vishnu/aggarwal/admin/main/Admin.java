package com.vishnu.aggarwal.admin.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

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
        /*setProperty("spring.devtools.restart.enabled", "true");
        setProperty("spring.devtools.livereload.enabled", "true");*/
        run(Admin.class, args);
    }
}
