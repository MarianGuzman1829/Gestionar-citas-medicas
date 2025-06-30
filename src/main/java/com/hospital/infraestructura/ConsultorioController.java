// src/main/java/com/hospital/infraestructura/ConsultorioController.java
package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Consultorio;
import com.hospital.dominio.entidades.Estatus;
import com.hospital.servicios.ConsultorioService;
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
@RequestMapping("/api/consultorios")
public class ConsultorioController {

    @Autowired
    private ConsultorioService consultorioService;

    @Autowired
    private EstatusService estatusService;

    // ... (Los métodos GET, POST, y DELETE no cambian y están correctos) ...
    @GetMapping("/")
    public List<Consultorio> getAll() {
        return consultorioService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Consultorio c = consultorioService.read(id);
        if (c == null) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","Consultorio no encontrado con ID: "+id);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Consultorio create(@RequestBody Consultorio consultorio) {
        return consultorioService.save(consultorio);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Consultorio consultorio) {
        Consultorio existing = consultorioService.read(id);
        if (existing == null) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","No se puede editar. Consultorio con ID "+id+" no existe.");
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }

        if (consultorio.getEstatus() == null || consultorio.getEstatus().getIdEstado() == null) { // <-- CORRECCIÓN AQUÍ
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje", "El ID del estatus es obligatorio para la actualización.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Long estatusId = consultorio.getEstatus().getIdEstado(); // <-- CORRECCIÓN AQUÍ
        Estatus estatusGestionado = estatusService.read(estatusId);

        if (estatusGestionado == null) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","El Estatus con ID "+estatusId+" no existe y no se puede asignar.");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        existing.setNumero(consultorio.getNumero());
        existing.setPiso(consultorio.getPiso());
        existing.setDescripcion(consultorio.getDescripcion());
        existing.setEstatus(estatusGestionado);

        Consultorio updated = consultorioService.save(existing);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        consultorioService.delete(id);
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<?> getByNumero(@PathVariable String numero) {
        Optional<Consultorio> opt = consultorioService.findByNumero(numero);
        if (opt.isEmpty()) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","No se encontró consultorio con número: "+numero);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(opt.get(), HttpStatus.OK);
    }

    @GetMapping("/estatus/{estatusId}")
    public ResponseEntity<?> getByEstatus(@PathVariable Long estatusId) {
        List<Consultorio> list = consultorioService.findByEstatus(estatusId);
        if (list.isEmpty()) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","No se encontraron consultorios con estatus ID: "+estatusId);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/piso/{piso}")
    public ResponseEntity<?> getByPiso(@PathVariable int piso) {
        List<Consultorio> list = consultorioService.findByPiso(piso);
        if (list.isEmpty()) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje","No se encontraron consultorios en el piso: "+piso);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}