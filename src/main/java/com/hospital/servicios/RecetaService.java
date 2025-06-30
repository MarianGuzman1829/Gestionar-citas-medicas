package com.hospital.servicios;

import com.hospital.dominio.entidades.Receta;
import java.util.List;

public interface RecetaService {
    List<Receta> readAll(); // Obtener todas las recetas
    Receta read(Long id); // Obtener receta por ID
    Receta save(Receta receta); // Guardar o actualizar receta
    void delete(Long id); // Eliminar receta por ID
    List<Receta> findByPaciente(Long idPaciente); // Buscar recetas por paciente
    List<Receta> findByDoctor(Long idDoctor); // Buscar recetas por doctor
}
