// src/main/java/com/hospital/config/SecurityConfig.java
package com.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuración de la codificación de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usamos BCrypt para encriptar contraseñas
    }

    // Elimina el bean HttpSecurity y dejar que Spring Security lo configure automáticamente
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().permitAll()  // Permite todas las solicitudes sin autenticación
            )
            .csrf(csrf -> csrf.disable());  // Deshabilita CSRF para facilitar las pruebas

        return http.build();
    }
}
