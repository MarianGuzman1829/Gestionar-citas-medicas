package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Medicamento;
import com.hospital.servicios.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    // Obtener todos los medicamentos
    @GetMapping
    public List<Medicamento> getAllMedicamentos() {
        return medicamentoService.readAll();
    }

    // Obtener medicamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> getMedicamentoById(@PathVariable Long id) {
        Medicamento medicamento = medicamentoService.read(id);
        if (medicamento != null) {
            return ResponseEntity.ok(medicamento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar medicamentos por nombre
    @GetMapping("/search")
    public List<Medicamento> searchMedicamentos(@RequestParam String nombre) {
        return medicamentoService.searchByName(nombre);
    }

    // Guardar un medicamento
    @PostMapping
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody Medicamento medicamento) {
        Medicamento savedMedicamento = medicamentoService.save(medicamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedicamento);
    }

    // Editar un medicamento existente
    @PutMapping("/{id}")
    public ResponseEntity<Medicamento> updateMedicamento(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        // Verificar si el medicamento existe
        Medicamento existingMedicamento = medicamentoService.read(id);
            if (existingMedicamento != null) {
                // Si existe, actualizar los campos
                existingMedicamento.setNombre(medicamento.getNombre());
                existingMedicamento.setPresentacion(medicamento.getPresentacion());
                existingMedicamento.setDescripcion(medicamento.getDescripcion());
                // Guardar el medicamento actualizado
                Medicamento updatedMedicamento = medicamentoService.save(existingMedicamento);
                return ResponseEntity.ok(updatedMedicamento);
        } else {
        // Si no existe, devolver un 404 Not Found
        return ResponseEntity.notFound().build();
        }
    }


    // Eliminar medicamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicamento(@PathVariable Long id) {
        medicamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
