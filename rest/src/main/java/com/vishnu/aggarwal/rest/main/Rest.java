package com.vishnu.aggarwal.rest.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

/**
 * The type Rest.
 */
@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
@EnableScheduling
public class Rest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        /*setProperty("spring.devtools.restart.enabled", "true");
        setProperty("spring.devtools.livereload.enabled", "true");*/
        run(Rest.class, args);
    }
}
