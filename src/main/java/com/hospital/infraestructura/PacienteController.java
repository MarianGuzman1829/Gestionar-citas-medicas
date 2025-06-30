package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Paciente;
import com.hospital.servicios.EmailService;
import com.hospital.servicios.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private EmailService emailService;

    // Obtener todos los pacientes
    @GetMapping("/")
    public List<Paciente> getAll() {
        return pacienteService.readAll();
    }

    // Obtener un paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Paciente paciente = pacienteService.read(id);
        if (paciente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Paciente no encontrado con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    // Crear un nuevo paciente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Paciente create(@RequestBody Paciente paciente) {
        // Primero guardamos el paciente para obtener el objeto completo con su ID
        Paciente pacienteGuardado = pacienteService.save(paciente);

        // Enviar correo de bienvenida 
        try {
            if (pacienteGuardado.getUsuario() != null) {
                String nombre = pacienteGuardado.getUsuario().getNombre();
                String email = pacienteGuardado.getUsuario().getEmail();
                emailService.enviarNotificacionBienvenida(email, nombre);
            }
        } catch (Exception e) {
            // Registrar el error pero no detener el flujo si el correo falla
            System.err.println("El paciente se guardó correctamente, pero falló el envío del correo de bienvenida: " + e.getMessage());
        }
        return pacienteGuardado;
    }

    // Actualizar un paciente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Paciente paciente) {
        Paciente existingPaciente = pacienteService.read(id);
        if (existingPaciente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se pudo editar. Paciente con ID " + id + " no existe.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Actualizamos los campos
        existingPaciente.setCurp(paciente.getCurp());
        existingPaciente.setFechaNacimiento(paciente.getFechaNacimiento());
        existingPaciente.setTipoSangre(paciente.getTipoSangre());
        existingPaciente.setAlergias(paciente.getAlergias());

        return new ResponseEntity<>(pacienteService.save(existingPaciente), HttpStatus.OK);
    }

    // Eliminar un paciente
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        pacienteService.delete(id);
    }

    // Buscar un paciente por CURP
    @GetMapping("/curp/{curp}")
    public ResponseEntity<?> getByCurp(@PathVariable String curp) {
        Paciente paciente = pacienteService.findByCurp(curp);
        if (paciente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontró paciente con CURP: " + curp);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }
}
