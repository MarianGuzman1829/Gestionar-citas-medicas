package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    // Buscar un horario exacto por su hora
    Optional<Horario> findByHorario(LocalTime horario);

    // Buscar un horario por su ID
    Optional<Horario> findByIdHorario(Long idHorario);

    List<Horario> findAllByOrderByIdHorarioAsc();
}