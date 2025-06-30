package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Doctor;
import com.hospital.dominio.entidades.Paciente;
import com.hospital.dominio.entidades.Receta;
import com.hospital.servicios.DoctorService;
import com.hospital.servicios.PacienteService;
import com.hospital.servicios.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PacienteService pacienteService;

    // --- MÉTODOS GET y DELETE (SIN CAMBIOS, ESTÁN CORRECTOS) ---

    @GetMapping
    @Transactional(readOnly = true)
    public List<Receta> getAllRecetas() {
        List<Receta> recetas = recetaService.readAll();
        // Forzamos la carga de datos lazy para evitar errores en la serialización
        recetas.forEach(receta -> {
            if (receta.getDoctor() != null) receta.getDoctorDetails();
            if (receta.getPaciente() != null) receta.getPacienteDetails();
            if (receta.getRecetaDetalles() != null) receta.getRecetaDetalles().size();
        });
        return recetas;
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Receta> getRecetaById(@PathVariable Long id) {
        Receta receta = recetaService.read(id);
        if (receta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receta);
    }

    @GetMapping("/paciente/{idPaciente}")
    @Transactional(readOnly = true)
    public List<Receta> getRecetasByPaciente(@PathVariable Long idPaciente) {
        return recetaService.findByPaciente(idPaciente);
    }

    @GetMapping("/doctor/{idDoctor}")
    @Transactional(readOnly = true)
    public List<Receta> getRecetasByDoctor(@PathVariable Long idDoctor) {
        return recetaService.findByDoctor(idDoctor);
    }

    // --- MÉTODO POST RECONSTRUIDO CON EL PATRÓN ROBUSTO ---
    @PostMapping
    public ResponseEntity<?> createReceta(@RequestBody Map<String, Object> request) {
        try {
            // 1. Extraer datos del JSON aplanado
            BigDecimal costoConsulta = new BigDecimal(request.get("costoConsulta").toString());
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse((String)request.get("fecha"));
            Long doctorId = Long.valueOf(request.get("doctorId").toString());
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());

            // 2. Validar que Doctor y Paciente existan
            Doctor doctor = doctorService.read(doctorId);
            if (doctor == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Doctor con ID " + doctorId + " no existe."));
            }

            Paciente paciente = pacienteService.read(pacienteId);
            if (paciente == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Paciente con ID " + pacienteId + " no existe."));
            }

            // 3. Crear el nuevo objeto Receta
            Receta nuevaReceta = new Receta();
            nuevaReceta.setCostoConsulta(costoConsulta);
            nuevaReceta.setFecha(fecha);
            nuevaReceta.setDoctor(doctor);       // Asignamos el objeto Doctor completo
            nuevaReceta.setPaciente(paciente);   // Asignamos el objeto Paciente completo

            // 4. Guardar la nueva receta
            Receta recetaGuardada = recetaService.save(nuevaReceta);
            return ResponseEntity.status(HttpStatus.CREATED).body(recetaGuardada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la petición: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceta(@PathVariable Long id) {
        // Sería bueno añadir una verificación para devolver 404 si no existe
        if (recetaService.read(id) == null) {
            return ResponseEntity.notFound().build();
        }
        recetaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}