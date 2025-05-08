package edu.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class ApiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiServiceApplication.class, args);
    }
}
