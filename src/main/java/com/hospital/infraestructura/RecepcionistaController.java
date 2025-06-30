package com.hospital.infraestructura;

import com.hospital.dominio.entidades.Consultorio;
import com.hospital.dominio.entidades.Horario;
import com.hospital.dominio.entidades.Recepcionista;
import com.hospital.dominio.entidades.TipoUsuario;
import com.hospital.dominio.entidades.Usuario;
import com.hospital.servicios.ConsultorioService;
import com.hospital.servicios.HorarioService;
import com.hospital.servicios.RecepcionistaService;
import com.hospital.servicios.TipoUsuarioService;
import com.hospital.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // <-- CAMBIO 1: Añadir este import
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;
    
    @Autowired
    private UsuarioService usuarioService; 
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    @Autowired
    private ConsultorioService consultorioService;

    // --- MÉTODOS GET MODIFICADOS ---
    @GetMapping
    @Transactional(readOnly = true) // <-- CAMBIO 2: Añadir anotación para cargar datos perezosos
    public ResponseEntity<List<Recepcionista>> obtenerTodosLosRecepcionistas() {
        List<Recepcionista> recepcionistas = recepcionistaService.obtenerTodosLosRecepcionistas();
        return ResponseEntity.ok(recepcionistas);
    }

    @GetMapping("/{idRecepcionista}")
    @Transactional(readOnly = true) // <-- CAMBIO 3: Añadir anotación para cargar datos perezosos
    public ResponseEntity<Recepcionista> obtenerRecepcionistaPorId(@PathVariable Long idRecepcionista) {
        Optional<Recepcionista> recepcionista = recepcionistaService.obtenerRecepcionistaPorId(idRecepcionista);
        return recepcionista.map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- MÉTODOS POST, PUT y DELETE (SIN CAMBIOS) ---
    // Estos métodos ya son transaccionales por naturaleza en la capa de servicio
    // y no necesitan esta anotación aquí.
    
    @PostMapping
    public ResponseEntity<?> crearRecepcionista(@RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            String apellidoPat = (String) request.get("apellidoPat");
            String apellidoMat = (String) request.get("apellidoMat");
            String email = (String) request.get("email");
            String contrasena = (String) request.get("contrasena");
            String telefono = (String) request.get("telefono");
            Long horarioId = Long.valueOf(request.get("horarioId").toString());
            Long consultorioId = Long.valueOf(request.get("consultorioId").toString());

            Horario horario = horarioService.read(horarioId);
            if (horario == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El Horario con ID " + horarioId + " no existe."));
            }

            Consultorio consultorio = consultorioService.read(consultorioId);
            if (consultorio == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El Consultorio con ID " + consultorioId + " no existe."));
            }

            TipoUsuario tipoUsuario = tipoUsuarioService.findByNombre("Recepcionista");
            if (tipoUsuario == null) {
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "El Tipo de Usuario 'RECEPCIONISTA' no está configurado en la base de datos."));
            }
            
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidoPat(apellidoPat);
            nuevoUsuario.setApellidoMat(apellidoMat);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setContrasena(contrasena);
            nuevoUsuario.setTelefono(telefono);
            nuevoUsuario.setTipoUsuario(tipoUsuario);
            
            Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);

            Recepcionista nuevoRecepcionista = new Recepcionista();
            nuevoRecepcionista.setUsuario(usuarioGuardado);
            nuevoRecepcionista.setHorario(horario);
            nuevoRecepcionista.setConsultorio(consultorio);

            Recepcionista recepcionistaGuardado = recepcionistaService.crearRecepcionista(nuevoRecepcionista);
            return ResponseEntity.status(HttpStatus.CREATED).body(recepcionistaGuardado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la petición: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{idRecepcionista}")
    public ResponseEntity<Recepcionista> actualizarRecepcionista(@PathVariable Long idRecepcionista,
                                                                 @RequestBody Map<String, Object> updates) {
        Optional<Recepcionista> recepcionistaOpt = recepcionistaService.obtenerRecepcionistaPorId(idRecepcionista);
        if (recepcionistaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recepcionista recepcionistaExistente = recepcionistaOpt.get();
        Usuario usuarioExistente = recepcionistaExistente.getUsuario();

        if (updates.containsKey("nombre")) usuarioExistente.setNombre((String) updates.get("nombre"));
        if (updates.containsKey("apellidoPat")) usuarioExistente.setApellidoPat((String) updates.get("apellidoPat"));
        if (updates.containsKey("apellidoMat")) usuarioExistente.setApellidoMat((String) updates.get("apellidoMat"));
        if (updates.containsKey("email")) usuarioExistente.setEmail((String) updates.get("email"));
        if (updates.containsKey("telefono")) usuarioExistente.setTelefono((String) updates.get("telefono"));
        
        if (updates.containsKey("horarioId")) {
            Long horarioId = Long.valueOf(updates.get("horarioId").toString());
            Horario nuevoHorario = horarioService.read(horarioId);
            if(nuevoHorario != null) {
                recepcionistaExistente.setHorario(nuevoHorario);
            }
        }
        
        if (updates.containsKey("consultorioId")) {
            Long consultorioId = Long.valueOf(updates.get("consultorioId").toString());
            Consultorio nuevoConsultorio = consultorioService.read(consultorioId);
            if(nuevoConsultorio != null) {
                recepcionistaExistente.setConsultorio(nuevoConsultorio);
            }
        }
        
        Recepcionista recepcionistaActualizado = recepcionistaService.actualizarRecepcionista(recepcionistaExistente);
        return ResponseEntity.ok(recepcionistaActualizado);
    }

    @DeleteMapping("/{idRecepcionista}")
    public ResponseEntity<Void> eliminarRecepcionista(@PathVariable Long idRecepcionista) {
        recepcionistaService.eliminarRecepcionista(idRecepcionista);
        return ResponseEntity.noContent().build();
    }
}