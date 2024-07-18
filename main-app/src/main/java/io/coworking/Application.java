package io.coworking;

import io.coworking.logging.EnableLogging;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application класс для инициализации приложения.
 */
@EnableLogging
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API", version = "1.0", description = "API Documentation"))
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
