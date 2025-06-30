package com.hospital.infraestructura;

import com.hospital.dominio.entidades.*;
import com.hospital.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
public class CitaConsultaController {

    @Autowired
    private CitaConsultaService citaConsultaService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private EstatusService estatusService;
    @Autowired
    private ConsultorioService consultorioService;
    @Autowired
    private EspecialidadService especialidadService;
    @Autowired
    private EmailService emailService;

    // --- MÉTODOS GET (MEJORADOS CON @Transactional) ---
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> obtenerTodasLasCitas() {
        List<CitaConsulta> citas = citaConsultaService.obtenerTodasLasCitas();
        List<Map<String, Object>> citasResponse = citas.stream()
                .map(CitaConsulta::toResponseMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/doctor/{idDoctor}/fecha/{fecha}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> obtenerCitasPorDoctorYFecha(
            @PathVariable Long idDoctor,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha) {
        List<CitaConsulta> citas = citaConsultaService.obtenerCitasPorDoctorYFecha(idDoctor, fecha);
        List<Map<String, Object>> citasResponse = citas.stream()
                .map(CitaConsulta::toResponseMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/paciente/{idPaciente}/fecha/{fecha}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> obtenerCitasPorPacienteYFecha(
            @PathVariable Long idPaciente, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha) {
        List<CitaConsulta> citas = citaConsultaService.obtenerCitasPorPacienteYFecha(idPaciente, fecha);
        List<Map<String, Object>> citasResponse = citas.stream()
                .map(CitaConsulta::toResponseMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/{idCita}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> obtenerCitaPorId(@PathVariable Long idCita) {
        Optional<CitaConsulta> cita = citaConsultaService.obtenerCitaPorId(idCita);
        return cita.map(c -> ResponseEntity.ok(c.toResponseMap()))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- MÉTODO POST 
    @PostMapping
    public ResponseEntity<?> crearCita(@RequestBody Map<String, Object> request) {
        try {
            // 1. Extraer datos del JSON aplanado
            LocalDate fecha = LocalDate.parse((String) request.get("fecha"));
            LocalTime hora = LocalTime.parse((String) request.get("hora"));
            String motivo = (String) request.get("motivo");
            Long doctorId = Long.valueOf(request.get("doctorId").toString());
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());
            Long estatusId = Long.valueOf(request.get("estatusId").toString());
            Long consultorioId = Long.valueOf(request.get("consultorioId").toString());
            Long especialidadId = Long.valueOf(request.get("especialidadId").toString());

            // 2. Validar que todas las entidades relacionadas existan
            Doctor doctor = doctorService.read(doctorId);
            if (doctor == null) return ResponseEntity.badRequest().body(Map.of("error", "Doctor con ID " + doctorId + " no existe."));

            Paciente paciente = pacienteService.read(pacienteId);
            if (paciente == null) return ResponseEntity.badRequest().body(Map.of("error", "Paciente con ID " + pacienteId + " no existe."));

            Estatus estatus = estatusService.read(estatusId);
            if (estatus == null) return ResponseEntity.badRequest().body(Map.of("error", "Estatus con ID " + estatusId + " no existe."));

            Consultorio consultorio = consultorioService.read(consultorioId);
            if (consultorio == null) return ResponseEntity.badRequest().body(Map.of("error", "Consultorio con ID " + consultorioId + " no existe."));
            
            Especialidad especialidad = especialidadService.read(especialidadId);
            if (especialidad == null) return ResponseEntity.badRequest().body(Map.of("error", "Especialidad con ID " + especialidadId + " no existe."));

            // 3. Crear el nuevo objeto CitaConsulta
            CitaConsulta nuevaCita = new CitaConsulta();
            nuevaCita.setFecha(fecha);
            nuevaCita.setHora(hora);
            nuevaCita.setMotivo(motivo);
            nuevaCita.setDoctor(doctor);
            nuevaCita.setPaciente(paciente);
            nuevaCita.setEstatus(estatus);
            nuevaCita.setConsultorio(consultorio);
            nuevaCita.setEspecialidad(especialidad);

            // 4. Guardar la nueva cita
            CitaConsulta citaGuardada = citaConsultaService.crearCita(nuevaCita);

            // Enviar correo al crear cita 
            try {
                emailService.enviarNotificacionNuevaCita(citaGuardada);
            } catch (Exception e) {
                // registrar el error, pero no fallar la transacción
                // si solo el correo no se pudo enviar.
                System.err.println("La cita se guardó correctamente, pero falló el envío del correo de notificación: " + e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(citaGuardada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la petición: " + e.getMessage()));
        }
    }

    // --- MÉTODO PUT RECONSTRUIDO ---
    @PutMapping("/{idCita}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long idCita, @RequestBody Map<String, Object> updates) {
        Optional<CitaConsulta> citaOpt = citaConsultaService.obtenerCitaPorId(idCita);
        if (citaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CitaConsulta citaExistente = citaOpt.get();

        try {
            // Actualizar campos simples si vienen en el JSON
            if (updates.containsKey("fecha")) citaExistente.setFecha(LocalDate.parse((String) updates.get("fecha")));
            if (updates.containsKey("hora")) citaExistente.setHora(LocalTime.parse((String) updates.get("hora")));
            if (updates.containsKey("motivo")) citaExistente.setMotivo((String) updates.get("motivo"));

            // Actualizar relaciones (con validación) si se proporcionan
            if (updates.containsKey("estatusId")) {
                Long estatusId = Long.valueOf(updates.get("estatusId").toString());
                Estatus nuevoEstatus = estatusService.read(estatusId);
                if (nuevoEstatus == null) return ResponseEntity.badRequest().body(Map.of("error", "Estatus con ID " + estatusId + " no existe."));
                
                citaExistente.setEstatus(nuevoEstatus);

                // Enviar correo al cancelar cita 
                if ("Cancelada".equalsIgnoreCase(nuevoEstatus.getNombre())) {
                    try {
                        emailService.enviarNotificacionCancelacion(citaExistente, "El estatus de la cita fue actualizado a 'Cancelada'.");
                    } catch (Exception e) {
                        System.err.println("La cita se actualizó, pero falló el envío del correo de cancelación: " + e.getMessage());
                    }
                }
            }
            // Aquí podemos añadir lógica similar para cambiar doctor, paciente, etc.
            
            CitaConsulta citaActualizada = citaConsultaService.actualizarCita(citaExistente);
            return ResponseEntity.ok(citaActualizada);
            
        } catch (Exception e) {
             return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la actualización: " + e.getMessage()));
        }
    }

    // --- MÉTODO DELETE
    @DeleteMapping("/{idCita}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long idCita) {
        if (citaConsultaService.obtenerCitaPorId(idCita).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        citaConsultaService.eliminarCita(idCita);
        return ResponseEntity.noContent().build();
    }
}
