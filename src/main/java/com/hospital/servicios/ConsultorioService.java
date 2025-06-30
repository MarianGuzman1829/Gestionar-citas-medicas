package com.hospital.servicios;

import com.hospital.dominio.entidades.Consultorio;
import java.util.List;
import java.util.Optional;

public interface ConsultorioService {
    List<Consultorio> readAll();
    Consultorio read(Long id);
    Consultorio save(Consultorio consultorio);
    void delete(Long id); // Método para eliminar un consultorio por ID
    Optional<Consultorio> findByNumero(String numero); // Método para buscar por número
    List<Consultorio> findByEstatus(Long estatusId); // Método para buscar por estatus
    List<Consultorio> findByPiso(int piso);  // Método para buscar por piso
    Consultorio obtenerConsultorioPorId(Long idConsultorio);  // Nuevo método
}
