# Usa una imagen base de OpenJDK 17 (ajusta la versión si es necesario)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y el wrapper de Maven (si estás usando mvnw) al contenedor
COPY mvnw pom.xml ./

# Copia los archivos de tu código fuente al contenedor
COPY src /app/src

# Asegúrate de que el archivo mvnw sea ejecutable
RUN chmod +x mvnw


# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación con Spring Boot
CMD ["./mvnw", "spring-boot:run"]
