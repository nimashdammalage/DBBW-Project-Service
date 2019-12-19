package dbbwproject.serviceunit;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class ServiceunitApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceunitApplication.class, args);
    }
}
