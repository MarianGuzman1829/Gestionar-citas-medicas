package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Usuario;
import com.hospital.servicios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Usuario> readAll() {
        return usuarioService.readAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        Usuario usuario = null;
        Map<String, Object> response = new HashMap<>();

        try {
            usuario = usuarioService.read(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al obtener el usuario de la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (usuario == null) {
            response.put("mensaje", "El usuario con ID ".concat(id.toString()).concat(" no existe en la base de datos."));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    // Crear un nuevo usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario create(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
public ResponseEntity<?> update(@RequestBody Usuario usuario, @PathVariable Long id) {
    Usuario usuarioActual = usuarioService.read(id);
    Map<String, Object> response = new HashMap<>();

    if (usuarioActual == null) {
        response.put("mensaje", "No se pudo editar. El usuario con ID ".concat(id.toString()).concat(" no existe."));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    try {
        // Actualizar los demás campos del usuario
        usuarioActual.setNombre(usuario.getNombre());
        usuarioActual.setApellidoPat(usuario.getApellidoPat());
        usuarioActual.setApellidoMat(usuario.getApellidoMat());
        usuarioActual.setEmail(usuario.getEmail());
        usuarioActual.setTelefono(usuario.getTelefono());
        usuarioActual.setTipoUsuario(usuario.getTipoUsuario());

        // Encriptar la contraseña antes de guardarla
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuarioActual.setContrasena(usuario.getContrasena());
            // lógica para encriptar la contraseña
            usuarioActual.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        } else if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            usuarioActual.setContrasena(usuarioActual.getContrasena()); // Mantener la contraseña actual si no se proporciona una nueva
        }
        

        Usuario usuarioActualizado = usuarioService.save(usuarioActual);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    } catch (DataAccessException e) {
        response.put("mensaje", "Error al actualizar el usuario.");
        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}