package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Horario;
import com.hospital.servicios.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/")
    public List<Horario> getAll() {
        return horarioService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Horario h = horarioService.read(id);
        if (h == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("mensaje", "Horario no encontrado con ID: " + id);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(h, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Horario horario) {
        // Verificar si es un horario de consulta (una hora específica)
        if (horario.getHorario() != null && horario.getHoraInicio() == null && horario.getHoraFin() == null) {
            // Es un horario para consulta, solo se necesita la hora
            horario.setHoraInicio(null);  // Asegurarse de que horaInicio y horaFin sean null
            horario.setHoraFin(null);
            return new ResponseEntity<>(horarioService.save(horario), HttpStatus.CREATED);
        }
        
        // Verificar si es un horario de consultorio (rango de horas)
        if (horario.getHoraInicio() != null && horario.getHoraFin() != null) {
            // Es un horario para consultorio, con rango de horas
            return new ResponseEntity<>(horarioService.save(horario), HttpStatus.CREATED);
        }
    
        // Si no se proporcionan los datos necesarios, responder con error
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Debe proporcionar una hora de consulta o un rango de horas para un consultorio.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Horario horario) {
        Horario existing = horarioService.read(id);
        if (existing == null) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("mensaje", "No se pudo editar. Horario con ID " + id + " no existe.");
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }

        // Verificar si estamos actualizando un horario de consulta o consultorio
        if (horario.getHorario() != null) {
            // Es un horario de consulta, solo actualizamos la hora
            existing.setHorario(horario.getHorario());
        } else if (horario.getHoraInicio() != null && horario.getHoraFin() != null) {
            // Es un horario de consultorio, actualizamos el rango de horas
            existing.setHoraInicio(horario.getHoraInicio());
            existing.setHoraFin(horario.getHoraFin());
        } else {
            // Si los datos no cumplen con la lógica de consulta o consultorio
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Debe proporcionar datos válidos para consulta o consultorio.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Guardamos y devolvemos el horario actualizado
        return new ResponseEntity<>(horarioService.save(existing), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        horarioService.delete(id);
    }
}
