package com.hospital.infraestructura;

import com.hospital.dominio.entidades.*; // Importamos las entidades
import com.hospital.servicios.*; // Importamos los servicios

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired private PdfService pdfService;
    @Autowired private DoctorService doctorService;
    @Autowired private PacienteService pacienteService;
    @Autowired private CitaConsultaService citaConsultaService;
    @Autowired private TipoUsuarioService tipoUsuarioService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private EspecialidadService especialidadService;
    @Autowired private EstatusService estatusService;
    @Autowired private ConsultorioService consultorioService;
    @Autowired private HorarioService horarioService;
    @Autowired private DoctorConsultorioService doctorConsultorioService;
    @Autowired private MedicamentoService medicamentoService;
    @Autowired private RecetaService recetaService;
    @Autowired private RecetaDetalleService recetaDetalleService;
    @Autowired private HistorialClinicoService historialClinicoService;
    @Autowired private RecepcionistaService recepcionistaService;
    @Autowired private EmailService emailService;
    // ... inyecta aquí los demás servicios que necesites para otros reportes

    // --- Endpoint para el reporte de Doctores ---
    @GetMapping(value = "/doctores/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteDoctores() {
        List<Doctor> doctores = doctorService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteDoctores(doctores);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-doctores.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- Endpoint para el reporte de Pacientes ---
    @GetMapping(value = "/pacientes/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reportePacientes() {
        List<Paciente> pacientes = pacienteService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReportePacientes(pacientes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-pacientes.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }
    
     // --- ENDPOINT PARA REPORTE DE TIPOS DE USUARIO ---
    @GetMapping(value = "/tipos-usuario/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteTiposUsuario() {
        List<TipoUsuario> tipos = tipoUsuarioService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteTiposUsuario(tipos);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-tipos-usuario.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE USUARIOS ---
    @GetMapping(value = "/usuarios/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteUsuarios() {
        List<Usuario> usuarios = usuarioService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteUsuarios(usuarios);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-usuarios.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE ESPECIALIDADES ---
    @GetMapping(value = "/especialidades/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteEspecialidades() {
        List<Especialidad> especialidades = especialidadService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteEspecialidades(especialidades);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-especialidades.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE ESTATUS ---
    @GetMapping(value = "/estatus/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteEstatus() {
        List<Estatus> estatusList = estatusService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteEstatus(estatusList);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-estatus.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE CONSULTORIOS ---
    @GetMapping(value = "/consultorios/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteConsultorios() {
        List<Consultorio> consultorios = consultorioService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteConsultorios(consultorios);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-consultorios.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE HORARIOS ---
    @GetMapping(value = "/horarios/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteHorarios() {
        List<Horario> horarios = horarioService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteHorarios(horarios);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-horarios.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

        // --- ENDPOINT PARA REPORTE DE ASIGNACIONES ---
    @GetMapping(value = "/asignaciones/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteAsignaciones() {
        List<DoctorConsultorio> asignaciones = doctorConsultorioService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteAsignaciones(asignaciones);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-asignaciones.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE MEDICAMENTOS ---
    @GetMapping(value = "/medicamentos/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteMedicamentos() {
        List<Medicamento> medicamentos = medicamentoService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteMedicamentos(medicamentos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-medicamentos.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE RECETAS ---
    @GetMapping(value = "/recetas/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteRecetas() {
        List<Receta> recetas = recetaService.readAll();
        ByteArrayInputStream pdf = pdfService.generarReporteRecetas(recetas);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-recetas.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

        // --- ENDPOINT PARA REPORTE DE RECETA DETALLE ---
    @GetMapping(value = "/receta-detalles/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteRecetaDetalles() {
        List<RecetaDetalle> detalles = recetaDetalleService.getAllRecetasDetalles();
        ByteArrayInputStream pdf = pdfService.generarReporteRecetaDetalles(detalles);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-receta-detalles.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    // --- ENDPOINT PARA REPORTE DE CITAS (VERSIÓN FINAL) ---
    @GetMapping(value = "/citas/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteCitas() {
        // Usamos el servicio de citas para obtener los datos
        List<CitaConsulta> citas = citaConsultaService.obtenerTodasLasCitas();
        ByteArrayInputStream pdf = pdfService.generarReporteCitas(citas);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-citas.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

        // --- ENDPOINT PARA REPORTE DE HISTORIAL CLÍNICO ---
    @GetMapping(value = "/historiales/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteHistoriales() {
        List<HistorialClinico> historiales = historialClinicoService.obtenerTodosLosHistoriales();
        
        // Es importante forzar la carga de datos perezosos aquí
        for(HistorialClinico h : historiales) {
            Hibernate.initialize(h.getDoctor().getUsuario());
            Hibernate.initialize(h.getPaciente().getUsuario());
            Hibernate.initialize(h.getReceta().getRecetaDetalles());
        }

        ByteArrayInputStream pdf = pdfService.generarReporteHistoriales(historiales);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-historiales-clinicos.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

        // --- ENDPOINT PARA REPORTE DE RECEPCIONISTAS ---
    @GetMapping(value = "/recepcionistas/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reporteRecepcionistas() {
        List<Recepcionista> recepcionistas = recepcionistaService.obtenerTodosLosRecepcionistas();
        
        // Forzamos la carga de datos para evitar errores de Lazy Loading
        for (Recepcionista r : recepcionistas) {
            Hibernate.initialize(r.getUsuario());
            Hibernate.initialize(r.getConsultorio());
            Hibernate.initialize(r.getHorario());
        }
        
        ByteArrayInputStream pdf = pdfService.generarReporteRecepcionistas(recepcionistas);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-recepcionistas.pdf");
        
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }

    @PostMapping("/historial/paciente/{idPaciente}/enviar-email")
    public ResponseEntity<?> enviarHistorialPorEmail(@PathVariable Long idPaciente) {
        
        // Este endpoint envía el historial clínico del paciente por correo electrónico
        try {
            // 1. Validar que el paciente exista
            Paciente paciente = pacienteService.read(idPaciente);
            if (paciente == null) {
                return new ResponseEntity<>(Map.of("error", "Paciente no encontrado con ID: " + idPaciente), HttpStatus.NOT_FOUND);
            }

            // 2. Obtener los historiales del paciente
            List<HistorialClinico> historiales = historialClinicoService.obtenerHistorialesPorPaciente(idPaciente);
            if (historiales.isEmpty()) {
                return new ResponseEntity<>(Map.of("mensaje", "El paciente no tiene historiales clínicos registrados."), HttpStatus.OK);
            }
            
            // Es importante forzar la carga de datos perezosos aquí
            for(HistorialClinico h : historiales) {
                if(h.getDoctor() != null) Hibernate.initialize(h.getDoctor().getUsuario());
                if(h.getPaciente() != null) Hibernate.initialize(h.getPaciente().getUsuario());
                if(h.getReceta() != null) Hibernate.initialize(h.getReceta().getRecetaDetalles());
            }

            // 3. Generar el PDF en memoria
            ByteArrayInputStream pdfStream = pdfService.generarReporteHistoriales(historiales);
            byte[] pdfBytes = pdfStream.readAllBytes(); 
            
            // 4. Preparar y enviar el correo
            String destinatario = paciente.getUsuario().getEmail();
            String asunto = "Su Historial Clínico - Clínica Salud y Bienestar";
            String nombreArchivo = "HistorialClinico_" + paciente.getUsuario().getApellidoPat() + ".pdf";
            
            String cuerpoEmail = "<h1>Envío de su Historial Clínico</h1>"
                               + "<p>Estimado/a " + paciente.getUsuario().getNombre() + ",</p>"
                               + "<p>Adjunto a este correo encontrará su historial clínico en formato PDF, tal como lo solicitó.</p>"
                               + "<p>Cuide su salud.</p>"
                               + "<p>Atentamente,<br>Clínica Salud y Bienestar</p>";

            emailService.enviarEmailConAdjunto(destinatario, asunto, cuerpoEmail, pdfBytes, nombreArchivo);

            return ResponseEntity.ok(Map.of("mensaje", "El historial clínico ha sido enviado exitosamente al correo: " + destinatario));

        } catch (Exception e) {
            // Este catch es más útil porque atrapará cualquier error inesperado durante el proceso.
            e.printStackTrace(); // Imprime el error en la consola paRA depuración
            return new ResponseEntity<>(Map.of("error", "Ocurrió un error interno al procesar la solicitud."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- Sigue este patrón para añadir los endpoints de los demás reportes ---

}
