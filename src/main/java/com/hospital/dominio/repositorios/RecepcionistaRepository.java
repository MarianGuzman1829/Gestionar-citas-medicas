package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; 
import java.util.List;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
    // Cargar recepcionista por su usuario
    Optional<Recepcionista> findByUsuario_IdUsuario(Long idUsuario);

    // Cargar recepcionista asignado a un consultorio
    Optional<Recepcionista> findByConsultorio_IdConsultorio(Long idConsultorio);

    // Cargar recepcionista asignado a un horario
    Optional<Recepcionista> findByHorario_IdHorario(Long idHorario);

    List<Recepcionista> findAllByOrderByIdRecepcionistaAsc();
}