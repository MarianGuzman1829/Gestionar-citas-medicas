package com.hospital.servicios;

import com.hospital.dominio.entidades.Especialidad;
import java.util.List;

public interface EspecialidadService {
    List<Especialidad> readAll();
    Especialidad read(Long id);
    Especialidad save(Especialidad especialidad);
    void delete(Long id);
    Especialidad findByNombre(String nombre);
    // Agregar el método obtenerEspecialidadPorId
    Especialidad obtenerEspecialidadPorId(Long idEspecialidad);
}
