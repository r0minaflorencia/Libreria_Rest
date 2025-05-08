package com.libreria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		// Pasarlas como System env para que Spring las use
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("DB_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(DemoApplication.class, args);
	}

}
