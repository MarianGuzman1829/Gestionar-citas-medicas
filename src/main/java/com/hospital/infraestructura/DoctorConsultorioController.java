package com.hospital.infraestructura;

import com.hospital.dominio.entidades.DoctorConsultorio;
import com.hospital.dominio.entidades.DoctorConsultorioId;
import com.hospital.servicios.DoctorConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones")
public class DoctorConsultorioController {

    @Autowired
    private DoctorConsultorioService dcService;

    // --- GET y POST sin cambios ---
    @GetMapping("/")
    public List<DoctorConsultorio> getAll() {
        return dcService.readAll();
    }
    // ... otros GET ...

    @PostMapping
    public DoctorConsultorio createAssignment(@RequestBody Map<String, Long> request) {
        Long doctorId = request.get("doctorId");
        Long consultorioId = request.get("consultorioId");
        Long horarioId = request.get("horarioId");
        return dcService.createAssignment(doctorId, consultorioId, horarioId);
    }

    // --- MÉTODO PUT CORREGIDO ---
    @PutMapping
    public ResponseEntity<?> updateAssignment(@RequestParam Long oldDoctorId,
                                              @RequestParam Long oldConsultorioId,
                                              @RequestParam Long oldHorarioId,
                                              @RequestBody Map<String, Long> request) {
        
        DoctorConsultorioId oldId = new DoctorConsultorioId(oldDoctorId, oldConsultorioId, oldHorarioId);
        
        // El método de servicio se encargará de verificar si la asignación "old" existe.
        
        Long newDoctorId = request.get("doctorId");
        Long newConsultorioId = request.get("consultorioId");
        Long newHorarioId = request.get("horarioId");
        
        // Validamos que los nuevos IDs no sean nulos
        if (newDoctorId == null || newConsultorioId == null || newHorarioId == null) {
            return new ResponseEntity<>("Se requieren 'doctorId', 'consultorioId' y 'horarioId' en el cuerpo de la petición.", HttpStatus.BAD_REQUEST);
        }

        try {
            DoctorConsultorio updatedAssignment = dcService.updateAssignment(oldId, newDoctorId, newConsultorioId, newHorarioId);
            return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Captura las excepciones de "no encontrado" del servicio
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // --- MÉTODO DELETE CORREGIDO ---
    @DeleteMapping
    public ResponseEntity<Void> deleteAssignment(@RequestParam Long doctorId,
                                                 @RequestParam Long consultorioId,
                                                 @RequestParam Long horarioId) {
                                                     
        DoctorConsultorioId id = new DoctorConsultorioId(doctorId, consultorioId, horarioId);
        
        // 1. Verificar que la asignación que se quiere eliminar exista
        if (dcService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 2. Si existe, eliminarla
        dcService.deleteById(id);
        
        // 3. Devolver 204 No Content
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}