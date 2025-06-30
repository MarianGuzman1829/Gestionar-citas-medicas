package com.hospital.infraestructura;

import com.hospital.dominio.entidades.RecetaDetalle;
import com.hospital.servicios.RecetaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas-detalle")
public class RecetaDetalleController {

    @Autowired
    private RecetaDetalleService recetaDetalleService;

    // Obtener todos los detalles de las recetas
    @GetMapping
    public List<RecetaDetalle> getAllRecetasDetalles() {
        return recetaDetalleService.getAllRecetasDetalles();
    }

    // Obtener detalles de receta por ID de receta
    @GetMapping("/receta/{idReceta}")
    public List<RecetaDetalle> getRecetasDetallesByRecetaId(@PathVariable Long idReceta) {
        return recetaDetalleService.getRecetasDetallesByRecetaId(idReceta);
    }

    // Obtener un detalle de receta por su ID
    @GetMapping("/{idRecetaDetalle}")
    public ResponseEntity<RecetaDetalle> getRecetaDetalleById(@PathVariable Long idRecetaDetalle) {
        return recetaDetalleService.getRecetaDetalleById(idRecetaDetalle)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo detalle de receta
    @PostMapping
    public ResponseEntity<RecetaDetalle> createRecetaDetalle(@RequestBody RecetaDetalle recetaDetalle) {
        RecetaDetalle savedRecetaDetalle = recetaDetalleService.saveRecetaDetalle(recetaDetalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecetaDetalle);
    }

    // Eliminar un detalle de receta
    @DeleteMapping("/{idRecetaDetalle}")
    public ResponseEntity<Void> deleteRecetaDetalle(@PathVariable Long idRecetaDetalle) {
        recetaDetalleService.deleteRecetaDetalle(idRecetaDetalle);
        return ResponseEntity.noContent().build();
    }
}
