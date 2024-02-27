package com.exercise.got;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGotApplication {

    public static void main(String[] args) {
        SpringApplication.from(GotApplication::main).with(TestGotApplication.class).run(args);
    }

}
