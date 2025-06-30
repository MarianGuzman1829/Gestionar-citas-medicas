package com.hospital.servicios;

import com.hospital.dominio.entidades.RecetaDetalle;
import java.util.List;
import java.util.Optional;

public interface RecetaDetalleService {
    List<RecetaDetalle> getAllRecetasDetalles(); // Obtener todos los detalles de las recetas
    List<RecetaDetalle> getRecetasDetallesByRecetaId(Long idReceta); // Obtener detalles de receta por ID de receta
    Optional<RecetaDetalle> getRecetaDetalleById(Long idRecetaDetalle); // Obtener detalle de receta por ID
    RecetaDetalle saveRecetaDetalle(RecetaDetalle recetaDetalle); // Guardar un nuevo detalle de receta
    void deleteRecetaDetalle(Long idRecetaDetalle); // Eliminar un detalle de receta
}
