package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    // Buscar medicamentos cuyo nombre contenga el fragmento (case-insensitive)
    List<Medicamento> findByNombreContainingIgnoreCase(String fragmento);

    // Buscar un medicamento por su ID
    Optional<Medicamento> findByIdMedicamento(Long idMedicamento);

    List<Medicamento> findAllByOrderByIdMedicamentoAsc();
}