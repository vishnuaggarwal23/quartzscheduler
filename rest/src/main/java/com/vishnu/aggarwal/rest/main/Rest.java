package com.vishnu.aggarwal.rest.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan("com.vishnu.aggarwal")
@EnableScheduling
//@EnableCaching
public class Rest {
    public static void main(String[] args) {
        /*setProperty("spring.devtools.restart.enabled", "true");
        setProperty("spring.devtools.livereload.enabled", "true");*/
        run(Rest.class, args);
    }
}
