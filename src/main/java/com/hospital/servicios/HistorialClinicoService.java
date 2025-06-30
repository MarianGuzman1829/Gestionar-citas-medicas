package com.hospital.servicios;

import com.hospital.dominio.entidades.HistorialClinico;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface HistorialClinicoService {
    // Obtener todos los historiales clínicos de un paciente
    List<HistorialClinico> obtenerHistorialesPorPaciente(Long idPaciente);

    // Obtener todos los historiales clínicos
    List<HistorialClinico> obtenerTodosLosHistoriales();

    // Obtener historial clínico por ID
    Optional<HistorialClinico> obtenerHistorialPorId(Long idHistorial);

    // Crear un nuevo historial clínico
    HistorialClinico crearHistorialClinico(HistorialClinico historialClinico);

    // Actualizar un historial clínico existente
    HistorialClinico actualizarHistorialClinico(HistorialClinico historialClinico);

    // Eliminar un historial clínico
    void eliminarHistorialClinico(Long idHistorial);

      /**
     * Orquesta el guardado de un archivo y lo asocia a un historial clínico
     * dentro de una única transacción.
     * @param idHistorial El ID del historial a actualizar.
     * @param file El archivo a guardar.
     */
    void guardarYAsociarArchivo(Long idHistorial, MultipartFile file);
}
