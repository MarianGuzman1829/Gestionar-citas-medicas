package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
    // Buscar una especialidad por su nombre
    Optional<Especialidad> findByNombre(String nombre);

    // Buscar una especialidad por su ID
    Optional<Especialidad> findById(Long id);

    List<Especialidad> findAllByOrderByIdEspecialidadAsc();
}