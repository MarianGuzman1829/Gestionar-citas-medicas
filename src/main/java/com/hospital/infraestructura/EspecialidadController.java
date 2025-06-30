package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Especialidad;
import com.hospital.servicios.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/")
    public List<Especialidad> getAll() {
        return especialidadService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Especialidad especialidad = especialidadService.read(id);
        if (especialidad == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Especialidad no encontrada con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(especialidad, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidad create(@RequestBody Especialidad especialidad) {
        return especialidadService.save(especialidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Especialidad especialidad) {
        Especialidad existingEspecialidad = especialidadService.read(id);
        if (existingEspecialidad == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Especialidad no encontrada con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        existingEspecialidad.setNombre(especialidad.getNombre());
        return new ResponseEntity<>(especialidadService.save(existingEspecialidad), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        especialidadService.delete(id);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        Especialidad especialidad = especialidadService.findByNombre(nombre);
        if (especialidad == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontró especialidad con el nombre: " + nombre);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(especialidad, HttpStatus.OK);
    }
}