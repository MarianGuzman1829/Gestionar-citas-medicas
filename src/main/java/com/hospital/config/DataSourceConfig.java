package com.hospital.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream; // Añadir import
import java.nio.file.Files; // Añadir import
import java.nio.file.Path;  // Añadir import
import java.nio.file.StandardCopyOption; // Añadir import

@Configuration
public class DataSourceConfig {

    private final ResourceLoader resourceLoader;

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    public DataSourceConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @Primary // Marca este bean como el DataSource principal
    public HikariDataSource dataSource() throws IOException {
        HikariDataSource dataSource = new HikariDataSource();

        // Configura la URL, usuario y contraseña desde las variables de entorno
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        // --- INICIO DE LA MODIFICACIÓN ---

        // 1. Cargar el recurso root.crt del classpath
        Resource resource = resourceLoader.getResource("classpath:root.crt");

        if (!resource.exists()) {
            throw new IOException("El archivo root.crt no se encontró en el classpath: " + resource.getDescription());
        }

        // Crear un archivo temporal para el certificado
        Path tempCertFile = null;
        try (InputStream inputStream = resource.getInputStream()) {
            // Se crea un archivo temporal que se eliminará al salir de la JVM
            tempCertFile = Files.createTempFile("root", ".crt");
            Files.copy(inputStream, tempCertFile, StandardCopyOption.REPLACE_EXISTING);
            tempCertFile.toFile().deleteOnExit(); // Asegura que el archivo temporal se elimine al finalizar la aplicación

            // Obtener la ruta absoluta del archivo temporal
            String rootCertAbsolutePath = tempCertFile.toAbsolutePath().toString();

            // Establecer la propiedad sslrootcert en HikariDataSource con la ruta absoluta del archivo TEMPORAL
            dataSource.addDataSourceProperty("sslrootcert", rootCertAbsolutePath);

        } catch (IOException e) {
            // Manejar cualquier error durante la copia o acceso al archivo
            throw new IOException("Error al procesar el certificado root.crt: " + e.getMessage(), e);
        }

        // --- FIN DE LA MODIFICACIÓN ---

        // Aseguramos que sslmode=verify-ca se mantenga si no está en la URL
        dataSource.addDataSourceProperty("sslmode", "verify-ca");

        return dataSource;
    }
}