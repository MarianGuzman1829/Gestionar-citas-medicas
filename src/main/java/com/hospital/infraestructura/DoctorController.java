package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Doctor;
import com.hospital.dominio.entidades.Especialidad;
import com.hospital.servicios.DoctorService;
import com.hospital.servicios.EmailService;
import com.hospital.servicios.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public List<Doctor> getAll() {
        return doctorService.readAll();
    }

    @GetMapping("/{id}")
    @Transactional  // Para inicializar fetch = LAZY si hiciera falta
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Doctor doctor = doctorService.read(id);
        if (doctor == null) {
            return new ResponseEntity<>(
                Map.of("mensaje", "Doctor no encontrado con ID: " + id),
                HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    // ------------------- POST corregido -------------------
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Doctor doctor) {
        // 1. Extraer el ID de la especialidad que viene en el JSON
        Long idEsp = doctor.getEspecialidad().getIdEspecialidad();
        
        // 2. Buscar la Especialidad en base de datos
        Especialidad esp = especialidadService.read(idEsp);
        if (esp == null) {
            return new ResponseEntity<>(
                Map.of("error", "La especialidad con ID " + idEsp + " no existe"),
                HttpStatus.BAD_REQUEST);
        }

        // 3. Asociar la entidad recuperada al doctor
        doctor.setEspecialidad(esp);

        // 4. Guardar sólo el Doctor (el Usuario se guarda en cascada)
        Doctor saved = doctorService.save(doctor);

        try {
            emailService.enviarBienvenidaDoctor(saved);
        } catch (Exception e) {
            System.err.println("El doctor se guardó, pero falló el envío del correo de bienvenida: " + e.getMessage());
        }
        // 5. Retornar el doctor guardado con estado CREATED
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    // ------------------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Doctor doctor) {
        Doctor existing = doctorService.read(id);
        if (existing == null) {
            return new ResponseEntity<>(
                Map.of("mensaje", "Doctor no encontrado con ID: " + id),
                HttpStatus.NOT_FOUND);
        }

        // Asociar especialidad existente
        Long idEsp = doctor.getEspecialidad().getIdEspecialidad();
        Especialidad esp = especialidadService.read(idEsp);
        if (esp == null) {
            return new ResponseEntity<>(
                Map.of("error", "La especialidad con ID " + idEsp + " no existe"),
                HttpStatus.BAD_REQUEST);
        }

        existing.setNoCedula(doctor.getNoCedula());
        existing.setEspecialidad(esp);

        Doctor updated = doctorService.save(existing);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doctorService.delete(id);
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> getByEspecialidad(@PathVariable String especialidad) {
        List<Doctor> doctors = doctorService.findByEspecialidad(especialidad);
        if (doctors.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("mensaje", "No se encontraron doctores con la especialidad: " + especialidad),
                HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/cedula/{noCedula}")
    public ResponseEntity<?> getByNoCedula(@PathVariable String noCedula) {
        Doctor doctor = doctorService.findByNoCedula(noCedula);
        if (doctor == null) {
            return new ResponseEntity<>(
                Map.of("mensaje", "No se encontró doctor con cédula: " + noCedula),
                HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
}
