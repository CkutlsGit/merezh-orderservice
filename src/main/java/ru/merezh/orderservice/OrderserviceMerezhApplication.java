package ru.merezh.orderservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Orderservice Merezh",
                version = "v1.0",
                description = "Эндпоинты для взаимодействие с заказом"
        )
)
@SpringBootApplication
public class OrderserviceMerezhApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderserviceMerezhApplication.class, args);
	}

}
