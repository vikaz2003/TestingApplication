package com.vikas.testingapplication;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TestingApplication  {

    public static void main(String[] args) {
        SpringApplication.run(TestingApplication.class, args);
    }



}
