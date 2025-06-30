package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    // Listar todos los historiales de un paciente
    List<HistorialClinico> findByPaciente_IdPaciente(Long idPaciente);

    // Listar el historial asociado a una cita
    Optional<HistorialClinico> findByCita_IdCita(Long idCita);

    // Buscar un historial por su ID
    Optional<HistorialClinico> findByIdHistorial(Long idHistorial);

    // Método para verificar si existe un historial clínico con el ID
    boolean existsById(Long idHistorial);

    List<HistorialClinico> findAllByOrderByIdHistorialAsc();
}