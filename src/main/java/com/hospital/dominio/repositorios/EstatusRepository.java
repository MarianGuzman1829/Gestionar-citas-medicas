package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Estatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EstatusRepository extends JpaRepository<Estatus, Long> {
    // Buscar un estado por su nombre
    Optional<Estatus> findByNombre(String nombre);

    // Buscar un estado por su ID
    Optional<Estatus> findByIdEstado(Long idEstado);

    List<Estatus> findAllByOrderByIdEstadoAsc();
}