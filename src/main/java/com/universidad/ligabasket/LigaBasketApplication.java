package com.universidad.ligabasket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// Clase principal que arranca la aplicación Spring Boot
@SpringBootApplication
public class LigaBasketApplication {

    public static void main(String[] args) {
        SpringApplication.run(LigaBasketApplication.class, args);
        // Mensaje de confirmación en consola al iniciar el servidor
        System.out.println("\n  ===================================");
        System.out.println("  LIGA BALONCESTO U22 - INICIADA");
        System.out.println("  Puerto: 8081");
        System.out.println("  URL: http://localhost:8081");
        System.out.println("===================================\n");
    }

    // Bean para realizar peticiones HTTP a APIs externas (ej: OpenWeather)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
