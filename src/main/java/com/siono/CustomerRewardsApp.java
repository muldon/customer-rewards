package com.siono;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@SecurityScheme(name = "siono", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Customer Rewards API", version = "2.0", description = "Information of the CustomerRewards program API"))
public class CustomerRewardsApp {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRewardsApp.class, args);
	}
	
	
	
}