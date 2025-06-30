package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    // Buscar todas las recetas emitidas a un paciente por su ID
    List<Receta> findByPaciente_IdPaciente(Long idPaciente);

    // Buscar todas las recetas emitidas por un doctor por su ID
    List<Receta> findByDoctor_IdDoctor(Long idDoctor);

    // Buscar una receta por su ID
    Optional<Receta> findByIdReceta(Long idReceta);

    List<Receta> findAllByOrderByIdRecetaAsc();
}