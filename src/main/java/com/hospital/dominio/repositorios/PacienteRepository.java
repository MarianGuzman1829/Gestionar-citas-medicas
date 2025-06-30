package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Buscar un paciente por su CURP
    Optional<Paciente> findByCurp(String curp);

    // Buscar un paciente por su ID
    Optional<Paciente> findByIdPaciente(Long idPaciente);

    List<Paciente> findAllByOrderByIdPacienteAsc();
}