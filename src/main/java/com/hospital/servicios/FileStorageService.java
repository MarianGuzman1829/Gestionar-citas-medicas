package com.hospital.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    /**
     * Constructor que inicializa el servicio.
     * Lee la ruta del directorio de subida desde application.properties
     * y crea el directorio si no existe.
     * @param uploadDir La ruta del directorio obtenida de las propiedades.
     */
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para guardar los archivos subidos.", ex);
        }
    }

    /**
     * Guarda un archivo en el servidor, lee sus bytes y devuelve ambos.
     * @param file El archivo subido desde la petición HTTP.
     * @return Un objeto StoredFile con el nombre único y los datos del archivo.
     */
    public StoredFile storeFile(MultipartFile file) {
        // Generar un nombre de archivo único para evitar colisiones y por seguridad.
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            // Extraer la extensión del archivo original (ej. ".pdf")
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch(Exception e) {
            // Si no hay extensión, se deja en blanco.
            fileExtension = "";
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Validar que el nombre del archivo es seguro y no contiene ".."
            if(fileName.contains("..")) {
                throw new RuntimeException("Nombre de archivo inválido: " + originalFileName);
            }

            // Obtener los bytes del archivo para guardarlos en la base de datos
            byte[] fileData = file.getBytes();

            // Guardar el archivo físicamente en el disco del servidor
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Devolver nuestro objeto auxiliar con el nombre y los datos del archivo
            return new StoredFile(fileName, fileData);

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + originalFileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get("ruta/a/tu/directorio/archivos").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo cargar el archivo: " + fileName, ex);
        }
    }
}
