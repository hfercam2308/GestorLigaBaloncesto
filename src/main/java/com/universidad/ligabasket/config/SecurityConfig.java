package com.universidad.ligabasket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Configuración de seguridad y acceso (CSRF, Rutas protegidas)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define BCrypt como el algoritmo para encriptar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura qué rutas son públicas y cuáles requieren autenticación
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitado para facilitar el uso de la API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/register.html", "/css/**", "/js/**", "/html/**",
                                "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(basic -> {
                });

        return http.build();
    }
}
