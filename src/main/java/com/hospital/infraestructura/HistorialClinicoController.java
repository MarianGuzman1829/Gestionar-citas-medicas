package com.hospital.infraestructura;

import com.hospital.dominio.entidades.*; // Importa todas las entidades necesarias
import com.hospital.servicios.*; // Importa todos los servicios necesarios

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/historiales")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoService historialClinicoService;

    // Inyectamos los servicios necesarios para manejar las entidades relacionadas
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private CitaConsultaService citaConsultaService;
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private FileStorageService fileStorageService;

    // --- MÉTODOS GET (SIN CAMBIOS, ESTÁN CORRECTOS) ---
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<HistorialClinico>> obtenerHistorialesPorPaciente(@PathVariable Long idPaciente) {
        List<HistorialClinico> historiales = historialClinicoService.obtenerHistorialesPorPaciente(idPaciente);
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{idHistorial}")
    public ResponseEntity<HistorialClinico> obtenerHistorialPorId(@PathVariable Long idHistorial) {
        Optional<HistorialClinico> historial = historialClinicoService.obtenerHistorialPorId(idHistorial);
        return historial.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<HistorialClinico>> obtenerTodosLosHistoriales() {
        List<HistorialClinico> historiales = historialClinicoService.obtenerTodosLosHistoriales();
        return ResponseEntity.ok(historiales);
    }

    // --- MÉTODO POST COMPLETAMENTE RECONSTRUIDO ---
    @PostMapping
    public ResponseEntity<?> crearHistorialClinico(@RequestBody Map<String, Object> request) {
        try {
            // 1. Extraer IDs del JSON aplanado
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());
            Long doctorId = Long.valueOf(request.get("doctorId").toString());
            Long citaId = Long.valueOf(request.get("citaId").toString());
            Long recetaId = request.get("recetaId") != null ? Long.valueOf(request.get("recetaId").toString()) : null;

            // 2. Cargar las entidades relacionadas para asegurar que existen
            Paciente paciente = pacienteService.read(pacienteId);
            if (paciente == null) return ResponseEntity.badRequest().body(Map.of("error", "Paciente con ID " + pacienteId + " no existe."));

            Doctor doctor = doctorService.read(doctorId);
            if (doctor == null) return ResponseEntity.badRequest().body(Map.of("error", "Doctor con ID " + doctorId + " no existe."));
            
            CitaConsulta cita = citaConsultaService.obtenerCitaPorId(citaId)
                .orElseThrow(() -> new RuntimeException("Cita con ID " + citaId + " no encontrada."));

            Receta receta = null;
            if(recetaId != null) {
                receta = recetaService.read(recetaId);
                if (receta == null) return ResponseEntity.badRequest().body(Map.of("error", "Receta con ID " + recetaId + " no existe."));
            }

            // 3. Crear y poblar el nuevo objeto HistorialClinico
            HistorialClinico nuevoHistorial = new HistorialClinico();
            nuevoHistorial.setDiagnostico((String) request.get("diagnostico"));
            nuevoHistorial.setTratamiento((String) request.get("tratamiento"));
            nuevoHistorial.setNotas((String) request.get("notas"));
            nuevoHistorial.setFechaDiagnostico(LocalDate.parse((String) request.get("fechaDiagnostico")));
            
            if (request.get("fechaAlta") != null) {
                nuevoHistorial.setFechaAlta(LocalDate.parse((String) request.get("fechaAlta")));
            }

            // Asignar las entidades completas
            nuevoHistorial.setPaciente(paciente);
            nuevoHistorial.setDoctor(doctor);
            nuevoHistorial.setCita(cita); // o setCitaConsulta
            nuevoHistorial.setReceta(receta);

            // 4. Guardar el objeto usando el método de servicio original
            HistorialClinico historialGuardado = historialClinicoService.crearHistorialClinico(nuevoHistorial);
            return ResponseEntity.status(HttpStatus.CREATED).body(historialGuardado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la petición: " + e.getMessage()));
        }
    }

    // --- MÉTODO PUT MEJORADO ---
    @PutMapping("/{idHistorial}")
    public ResponseEntity<?> actualizarHistorialClinico(@PathVariable Long idHistorial,
                                                          @RequestBody Map<String, Object> updates) {
        
        // 1. Verificar que el historial a actualizar exista
        Optional<HistorialClinico> historialOpt = historialClinicoService.obtenerHistorialPorId(idHistorial);
        if (historialOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        HistorialClinico historialExistente = historialOpt.get();

        // 2. Actualizar campos simples si vienen en el JSON
        if (updates.containsKey("diagnostico")) historialExistente.setDiagnostico((String) updates.get("diagnostico"));
        if (updates.containsKey("tratamiento")) historialExistente.setTratamiento((String) updates.get("tratamiento"));
        if (updates.containsKey("notas")) historialExistente.setNotas((String) updates.get("notas"));
        if (updates.containsKey("fechaDiagnostico")) historialExistente.setFechaDiagnostico(LocalDate.parse((String) updates.get("fechaDiagnostico")));
        if (updates.containsKey("fechaAlta")) historialExistente.setFechaAlta(LocalDate.parse((String) updates.get("fechaAlta")));

        // 3. (Opcional) Actualizar relaciones si vienen en el JSON
        // Por ahora, lo dejamos sin actualizar relaciones para mantenerlo simple. Si se necesita, se añade aquí.

        // 4. Guardar el historial actualizado
        HistorialClinico historialActualizado = historialClinicoService.actualizarHistorialClinico(historialExistente);
        return ResponseEntity.ok(historialActualizado);
    }

    // --- MÉTODO DELETE (SIN CAMBIOS, ESTÁ CORRECTO) ---
    @DeleteMapping("/{idHistorial}")
    public ResponseEntity<Void> eliminarHistorialClinico(@PathVariable Long idHistorial) {
        // Sería bueno añadir una verificación de existencia aquí también para devolver 404 si no se encuentra
        historialClinicoService.eliminarHistorialClinico(idHistorial);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para subir un archivo y asociarlo a un historial clínico existente.
     * @param idHistorial El ID del historial clínico al que se adjuntará el archivo.
     * @param file El archivo enviado en la petición (ej. resultados.pdf).
     * @return Una respuesta indicando el éxito o fracaso de la operación.
     */
    @PostMapping("/{idHistorial}/uploadFile")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long idHistorial,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // --- INICIO DE LA MODIFICACIÓN: Delegar toda la lógica al servicio ---
            historialClinicoService.guardarYAsociarArchivo(idHistorial, file);
            // --- FIN DE LA MODIFICACIÓN ---
            
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Archivo subido y asociado al historial exitosamente."
            ));

        } catch (RuntimeException e) {
            // Capturar errores como "Historial no encontrado"
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "No se pudo subir el archivo. Error interno."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{idHistorial}/downloadFile")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long idHistorial) {
        
        // 1. Buscar el historial en la base de datos
        Optional<HistorialClinico> historialOpt = historialClinicoService.obtenerHistorialPorId(idHistorial);
        
        // 2. Validar que el historial y los datos del archivo existan
        if (historialOpt.isEmpty() || historialOpt.get().getArchivoAdjuntoData() == null || historialOpt.get().getArchivoAdjuntoPath() == null) {
            return ResponseEntity.notFound().build();
        }
        
        HistorialClinico historial = historialOpt.get();
        String fileName = historial.getArchivoAdjuntoPath();
        byte[] fileData = historial.getArchivoAdjuntoData();

        // 3. Crear un recurso a partir de los bytes guardados en la base de datos
        ByteArrayResource resource = new ByteArrayResource(fileData);

        // 4. Construir y devolver la respuesta para la descarga
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Tipo de contenido genérico para descarga
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}