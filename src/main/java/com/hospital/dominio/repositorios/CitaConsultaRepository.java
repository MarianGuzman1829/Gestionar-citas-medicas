package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.CitaConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaConsultaRepository extends JpaRepository<CitaConsulta, Long> {
    // Buscar todas las citas de un doctor en una fecha dada
    List<CitaConsulta> findByDoctor_IdDoctorAndFecha(Long idDoctor, LocalDate fecha);

    // Buscar todas las citas de un paciente en una fecha dada
    List<CitaConsulta> findByPaciente_IdPacienteAndFecha(Long idPaciente, LocalDate fecha);

    // Buscar una cita por su ID
    Optional<CitaConsulta> findByIdCita(Long idCita);

    List<CitaConsulta> findAllByOrderByIdCitaAsc();
}