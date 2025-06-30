package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Estatus;
import com.hospital.servicios.EstatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/estatus")
public class EstatusController {

    @Autowired
    private EstatusService estatusService;

    @GetMapping("/")
    public List<Estatus> getAll() {
        return estatusService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Estatus estatus = estatusService.read(id);
        if (estatus == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Estatus no encontrado con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(estatus, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estatus create(@RequestBody Estatus estatus) {
        return estatusService.save(estatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Estatus estatus) {
        Estatus existingEstatus = estatusService.read(id);
        if (existingEstatus == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Estatus no encontrado con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        existingEstatus.setNombre(estatus.getNombre());
        return new ResponseEntity<>(estatusService.save(existingEstatus), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        estatusService.delete(id);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        Optional<Estatus> estatusList = estatusService.findByNombre(nombre);
        if (estatusList.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontró estatus con el nombre: " + nombre);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(estatusList, HttpStatus.OK);
    }
}