package com.hospital.infraestructura;

import com.hospital.dominio.entidades.TipoUsuario;
import com.hospital.servicios.TipoUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.dao.DataAccessException;

@RestController
@RequestMapping("/api/tipousuarios")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    // Obtener todos los tipos de usuario
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TipoUsuario> readAll() {
        return tipoUsuarioService.readAll();
    }

    // Obtener un tipo de usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        TipoUsuario tipoUsuario = null;
        Map<String, Object> response = new HashMap<>();

        try {
            tipoUsuario = tipoUsuarioService.read(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al obtener el tipo de usuario de la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (tipoUsuario == null) {
            response.put("mensaje", "El tipo de usuario con ID ".concat(id.toString()).concat(" no existe en la base de datos."));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tipoUsuario, HttpStatus.OK);
    }

    // Crear un nuevo tipo de usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoUsuario create(@RequestBody TipoUsuario tipoUsuario) {
        return tipoUsuarioService.save(tipoUsuario);
    }

    // Actualizar un tipo de usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TipoUsuario tipoUsuario, @PathVariable Long id) {
        TipoUsuario tipoUsuarioActual = tipoUsuarioService.read(id);
        Map<String, Object> response = new HashMap<>();

        if (tipoUsuarioActual == null) {
            response.put("mensaje", "No se pudo editar. El tipo de usuario con ID ".concat(id.toString()).concat(" no existe."));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            tipoUsuarioActual.setNombre(tipoUsuario.getNombre());
            TipoUsuario tipoUsuarioActualizado = tipoUsuarioService.save(tipoUsuarioActual);
            return new ResponseEntity<>(tipoUsuarioActualizado, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el tipo de usuario.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un tipo de usuario
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tipoUsuarioService.delete(id);
    }
}