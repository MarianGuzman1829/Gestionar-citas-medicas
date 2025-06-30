package com.hospital;

// import io.github.cdimascio.dotenv.Dotenv; // <-- ELIMINAR ESTA LÍNEA
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AplicacionMedicaApplication {

    public static void main(String[] args) {
        // Carga el archivo .env y lo pone en las propiedades del sistema
        // Inicia la aplicación Spring
        SpringApplication.run(AplicacionMedicaApplication.class, args);
    }
}