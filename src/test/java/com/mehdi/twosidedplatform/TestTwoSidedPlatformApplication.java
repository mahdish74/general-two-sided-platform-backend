package com.mehdi.twosidedplatform;

import org.springframework.boot.SpringApplication;

public class TestTwoSidedPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.from(TwoSidedPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
