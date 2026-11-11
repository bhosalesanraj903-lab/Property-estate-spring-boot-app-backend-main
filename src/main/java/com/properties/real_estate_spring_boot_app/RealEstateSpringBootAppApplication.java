package com.properties.real_estate_spring_boot_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.properties.real_estate_spring_boot_app.repository")
@EntityScan(basePackages = "com.properties.real_estate_spring_boot_app.model")
public class RealEstateSpringBootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateSpringBootAppApplication.class, args);
	}

}
