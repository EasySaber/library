package com.example.sshomework;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SecurityScheme(
		name = "basicAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "basic"
)
@OpenAPIDefinition(
		info = @Info(title = "SsHomeWork API", version = "1.0", description = "API Services"),
		security = {@SecurityRequirement(name = "basicAuth")}
)
@SpringBootApplication()
public class SsHomeWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsHomeWorkApplication.class, args);
	}

}

