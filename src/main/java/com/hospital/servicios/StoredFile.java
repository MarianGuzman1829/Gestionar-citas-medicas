package com.hospital.servicios;

/**
 * Clase auxiliar para transportar la información de un archivo guardado.
 * Contiene tanto el nombre único generado como los datos binarios del archivo.
 */
public class StoredFile {
    private final String fileName;
    private final byte[] data;

    public StoredFile(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
