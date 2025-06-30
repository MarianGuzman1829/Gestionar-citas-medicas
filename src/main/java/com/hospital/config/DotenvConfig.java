// package com.hospital.config;

// import io.github.cdimascio.dotenv.Dotenv;
// import org.springframework.stereotype.Component;

// import javax.annotation.PostConstruct;

// @Component
// public class DotenvConfig {

//     @PostConstruct
//     public void init() {
//         // Cargar las variables de entorno desde el archivo .env
//         Dotenv dotenv = Dotenv.load();
        
//         // Establecer las variables de entorno en el sistema
//         System.setProperty("DB_URL", dotenv.get("DB_URL"));
//         System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//         System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//     }
// }
