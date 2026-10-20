package com.diego.lime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimeApplication.class, args);
    }

}
