package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Consultorio;
import com.hospital.dominio.entidades.Estatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    // Buscar un consultorio por su número
    Optional<Consultorio> findByNumero(String numero);

    // Listar todos los consultorios con un estado dado
    List<Consultorio> findByEstatus(Estatus estatus);

    // Listar todos los consultorios con un número específico
    List<Consultorio> findByPiso(int piso);

    // Listar todos los consultorios cuyo estado tenga un ID específico
    List<Consultorio> findByEstatus_IdEstado(Long idEstado);

    List<Consultorio> findAllByOrderByIdConsultorioAsc();
}