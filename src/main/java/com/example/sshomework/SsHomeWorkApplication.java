package com.example.sshomework;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SsHomeWork API", version = "1.0", description = "API Services"))
public class SsHomeWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsHomeWorkApplication.class, args);
	}

}

