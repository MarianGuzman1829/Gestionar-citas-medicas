package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.RecetaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaDetalleRepository extends JpaRepository<RecetaDetalle, Long> {
    // Listar todos los detalles de una receta por el ID de la receta
    List<RecetaDetalle> findByReceta_IdReceta(Long idReceta);

    // Buscar un detalle de receta por su ID
    Optional<RecetaDetalle> findByIdRecetaDetalle(Long idRecetaDetalle);

    List<RecetaDetalle> findAllByOrderByIdRecetaDetalleAsc();
}