package com.example.claudeapi.fee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.example.claudeapi.fee",
    "com.example.claudeapi.common"
})
public class FeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeeApplication.class, args);
    }

}
