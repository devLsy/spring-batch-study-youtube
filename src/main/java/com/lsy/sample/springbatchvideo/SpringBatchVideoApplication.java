package com.lsy.sample.springbatchvideo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchVideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchVideoApplication.class, args);
    }

}
