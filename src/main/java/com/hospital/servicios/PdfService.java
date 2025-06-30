package com.hospital.servicios;

import com.hospital.dominio.entidades.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    // --- Fuentes reutilizables para el documento ---
    private static final Font FONT_TITULO_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font FONT_HEADER_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9); // Un poco más pequeño
    private static final Font FONT_CELDA = FontFactory.getFont(FontFactory.HELVETICA, 8); // Un poco más pequeño

// --- MÉTODO PARA REPORTE DE DOCTORES (A PRUEBA DE FALLOS) ---
public ByteArrayInputStream generarReporteDoctores(List<Doctor> doctores) {
    Document document = new Document(PageSize.A4.rotate());
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
        PdfWriter.getInstance(document, out);
        document.open();
        // ... (código del título)

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        Stream.of("ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono", "Cédula", "Especialidad")
              .forEach(h -> table.addCell(crearCeldaHeader(h)));
        
        for (Doctor doctor : doctores) {
            table.addCell(new Phrase(doctor.getIdDoctor() != null ? doctor.getIdDoctor().toString() : "N/A", FONT_CELDA));

            if (doctor.getUsuario() != null) {
                table.addCell(new Phrase(doctor.getUsuario().getNombre(), FONT_CELDA));
                table.addCell(new Phrase(doctor.getUsuario().getApellidoPat(), FONT_CELDA));
                table.addCell(new Phrase(doctor.getUsuario().getApellidoMat() != null ? doctor.getUsuario().getApellidoMat() : "", FONT_CELDA));
                table.addCell(new Phrase(doctor.getUsuario().getEmail(), FONT_CELDA));
                table.addCell(new Phrase(doctor.getUsuario().getTelefono(), FONT_CELDA));
            } else {
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
            }

            table.addCell(new Phrase(doctor.getNoCedula(), FONT_CELDA));

            if (doctor.getEspecialidad() != null) {
                table.addCell(new Phrase(doctor.getEspecialidad().getNombre(), FONT_CELDA));
            } else {
                table.addCell(new Phrase("N/A", FONT_CELDA));
            }
        }
        document.add(table);
        document.close();
    } catch (Exception e) { // Captura cualquier excepción, no solo DocumentException
        e.printStackTrace();
    }
    return new ByteArrayInputStream(out.toByteArray());
}

// --- MÉTODO PARA REPORTE DE PACIENTES (A PRUEBA DE FALLOS) ---
public ByteArrayInputStream generarReportePacientes(List<Paciente> pacientes) {
    Document document = new Document(PageSize.A4.rotate());
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
        PdfWriter.getInstance(document, out);
        document.open();
        // ... (código del título)

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        Stream.of("ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono", "CURP", "T. Sangre", "Alergias")
              .forEach(h -> table.addCell(crearCeldaHeader(h)));

        for (Paciente paciente : pacientes) {
            table.addCell(new Phrase(paciente.getIdPaciente() != null ? paciente.getIdPaciente().toString() : "N/A", FONT_CELDA));
            
            if (paciente.getUsuario() != null) {
                table.addCell(new Phrase(paciente.getUsuario().getNombre(), FONT_CELDA));
                table.addCell(new Phrase(paciente.getUsuario().getApellidoPat(), FONT_CELDA));
                table.addCell(new Phrase(paciente.getUsuario().getApellidoMat() != null ? paciente.getUsuario().getApellidoMat() : "", FONT_CELDA));
                table.addCell(new Phrase(paciente.getUsuario().getEmail(), FONT_CELDA));
                table.addCell(new Phrase(paciente.getUsuario().getTelefono(), FONT_CELDA));
            } else {
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
                table.addCell(new Phrase("N/A", FONT_CELDA));
            }

            table.addCell(new Phrase(paciente.getCurp(), FONT_CELDA));
            table.addCell(new Phrase(paciente.getTipoSangre(), FONT_CELDA));
            table.addCell(new Phrase(paciente.getAlergias() != null ? paciente.getAlergias() : "Ninguna", FONT_CELDA));
        }
        document.add(table);
        document.close();
    } catch (Exception e) { // Captura cualquier excepción
        e.printStackTrace();
    }
    return new ByteArrayInputStream(out.toByteArray());
}

        // --- MÉTODO PARA REPORTE DE TIPOS DE USUARIO ---
    public ByteArrayInputStream generarReporteTiposUsuario(List<TipoUsuario> tiposUsuario) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Catálogo de Tipos de Usuario", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2); // 2 columnas
            table.setWidthPercentage(50); // Hacemos la tabla más pequeña
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            Stream.of("ID", "Nombre del Rol").forEach(h -> table.addCell(crearCeldaHeader(h)));
            
            for (TipoUsuario tipo : tiposUsuario) {
                table.addCell(new Phrase(tipo.getIdTipoUsuario().toString(), FONT_CELDA));
                table.addCell(new Phrase(tipo.getNombre(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

// --- MÉTODO PARA REPORTE DE USUARIOS (VERSIÓN FINAL Y DETALLADA) ---
    public ByteArrayInputStream generarReporteUsuarios(List<Usuario> usuarios) {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para que quepan más columnas
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Usuarios del Sistema", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            // CAMBIO: Ahora son 8 columnas
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            
            // CAMBIO: Se añaden las nuevas cabeceras
            Stream.of("ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono", "Tipo Usuario", "Contraseña (Hasheada)")
                  .forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (Usuario usuario : usuarios) {
                table.addCell(new Phrase(usuario.getIdUsuario().toString(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getNombre(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getApellidoPat(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getApellidoMat() != null ? usuario.getApellidoMat() : "", FONT_CELDA));
                table.addCell(new Phrase(usuario.getEmail(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getTelefono(), FONT_CELDA));
                
                // CAMBIO: Obtenemos el nombre del TipoUsuario, no el ID
                table.addCell(new Phrase(usuario.getTipoUsuario().getNombre(), FONT_CELDA));
                
                // CAMBIO: Se añade la contraseña
                table.addCell(new Phrase(usuario.getContrasena(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE ESPECIALIDADES ---
    public ByteArrayInputStream generarReporteEspecialidades(List<Especialidad> especialidades) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Catálogo de Especialidades Médicas", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            Stream.of("ID", "Nombre de la Especialidad").forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (Especialidad especialidad : especialidades) {
                table.addCell(new Phrase(especialidad.getIdEspecialidad().toString(), FONT_CELDA));
                table.addCell(new Phrase(especialidad.getNombre(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

     // --- MÉTODO PARA REPORTE DE ESTATUS ---
    public ByteArrayInputStream generarReporteEstatus(List<Estatus> estatusList) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Catálogo de Estatus", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2); // 2 columnas
            table.setWidthPercentage(50);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            Stream.of("ID Estatus", "Nombre").forEach(h -> table.addCell(crearCeldaHeader(h)));
            
            for (Estatus estatus : estatusList) {
                table.addCell(new Phrase(estatus.getIdEstado().toString(), FONT_CELDA));
                table.addCell(new Phrase(estatus.getNombre(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE CONSULTORIOS ---
    public ByteArrayInputStream generarReporteConsultorios(List<Consultorio> consultorios) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Consultorios", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5); // 5 columnas
            table.setWidthPercentage(100);
            Stream.of("ID", "Número", "Piso", "Descripción", "Estatus")
                  .forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (Consultorio consultorio : consultorios) {
                table.addCell(new Phrase(consultorio.getIdConsultorio().toString(), FONT_CELDA));
                table.addCell(new Phrase(consultorio.getNumero(), FONT_CELDA));
                table.addCell(new Phrase(consultorio.getPiso().toString(), FONT_CELDA));
                table.addCell(new Phrase(consultorio.getDescripcion() != null ? consultorio.getDescripcion() : "", FONT_CELDA));
                // Obtenemos el nombre del Estatus, no solo el ID
                table.addCell(new Phrase(consultorio.getEstatus().getNombre(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE HORARIOS ---
    public ByteArrayInputStream generarReporteHorarios(List<Horario> horarios) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Horarios y Asignaciones", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3); // 3 Columnas
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 4}); // Damos más espacio a la columna de asignaciones
            Stream.of("ID", "Horario", "Asignaciones Actuales").forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (Horario horario : horarios) {
                table.addCell(new Phrase(horario.getIdHorario().toString(), FONT_CELDA));

                // Formateamos la celda de la hora dependiendo del tipo de horario
                String textoHorario;
                if (horario.getHorario() != null) {
                    textoHorario = "Hora Cita: " + horario.getHorario().toString();
                } else if (horario.getHoraInicio() != null && horario.getHoraFin() != null) {
                    textoHorario = "Turno: " + horario.getHoraInicio().toString() + " - " + horario.getHoraFin().toString();
                } else {
                    textoHorario = "No definido";
                }
                table.addCell(new Phrase(textoHorario, FONT_CELDA));

                // Construimos un texto con todas las asignaciones de este horario
                StringBuilder asignaciones = new StringBuilder();
                if (horario.getDoctorConsultorios() != null && !horario.getDoctorConsultorios().isEmpty()) {
                    horario.getDoctorConsultorios().forEach(asig -> {
                        asignaciones.append("Dr. ").append(asig.getDoctor().getUsuario().getNombre())
                                    .append(" (Cons: ").append(asig.getConsultorio().getNumero()).append(")\n");
                    });
                }
                if (horario.getRecepcionistas() != null && !horario.getRecepcionistas().isEmpty()) {
                    horario.getRecepcionistas().forEach(recep -> {
                        asignaciones.append("Recep: ").append(recep.getUsuario().getNombre()).append("\n");
                    });
                }

                table.addCell(new Phrase(asignaciones.length() > 0 ? asignaciones.toString() : "Sin asignaciones", FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

         // --- MÉTODO PARA REPORTE DE ASIGNACIONES (VERSIÓN CORREGIDA) ---
    public ByteArrayInputStream generarReporteAsignaciones(List<DoctorConsultorio> asignaciones) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Reporte de Asignaciones de Doctores a Consultorios", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            for (DoctorConsultorio asignacion : asignaciones) {
                // Usamos una tabla para organizar la información de cada asignación
                PdfPTable tablaAsignacion = new PdfPTable(2); // <-- Nombre correcto de la tabla
                tablaAsignacion.setWidthPercentage(100);
                tablaAsignacion.setWidths(new float[]{1, 3});
                
                // Celda de Título para la asignación
                // CORRECCIÓN: Obtenemos los IDs desde las entidades relacionadas, es más seguro.
                String tituloAsignacion = "Asignación: Dr. ID " + asignacion.getDoctor().getIdDoctor() + 
                                          " - Cons. ID " + asignacion.getConsultorio().getIdConsultorio();
                PdfPCell cellTitulo = new PdfPCell(new Phrase(tituloAsignacion, FONT_HEADER_TABLA));
                cellTitulo.setColspan(2);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tablaAsignacion.addCell(cellTitulo);

                // Datos del Doctor
                tablaAsignacion.addCell(new Phrase("Doctor:", FONT_HEADER_TABLA));
                String docInfo = "Nombre: " + asignacion.getDoctor().getUsuario().getNombre() + " " + asignacion.getDoctor().getUsuario().getApellidoPat() +
                                 "\nCédula: " + asignacion.getDoctor().getNoCedula() +
                                 "\nEspecialidad: " + asignacion.getDoctor().getEspecialidad().getNombre();
                tablaAsignacion.addCell(new Phrase(docInfo, FONT_CELDA));

                // Datos del Consultorio
                tablaAsignacion.addCell(new Phrase("Consultorio:", FONT_HEADER_TABLA));
                String consInfo = "Número: " + asignacion.getConsultorio().getNumero() + " (Piso " + asignacion.getConsultorio().getPiso() + ")" +
                                  "\nDescripción: " + asignacion.getConsultorio().getDescripcion();
                tablaAsignacion.addCell(new Phrase(consInfo, FONT_CELDA));

                // Datos del Horario
                // CORRECCIÓN: Usamos 'tablaAsignacion' en lugar de 'table'
                tablaAsignacion.addCell(new Phrase("Horario:", FONT_HEADER_TABLA));
                String horarioInfo = "Turno: " + asignacion.getHorario().getHoraInicio() + " - " + asignacion.getHorario().getHoraFin();
                tablaAsignacion.addCell(new Phrase(horarioInfo, FONT_CELDA));

                document.add(tablaAsignacion);
                document.add(new Paragraph(" ")); // Espacio entre asignaciones
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE MEDICAMENTOS ---
    public ByteArrayInputStream generarReporteMedicamentos(List<Medicamento> medicamentos) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Catálogo de Medicamentos", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            Stream.of("ID", "Nombre Comercial", "Presentación", "Descripción").forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (Medicamento med : medicamentos) {
                table.addCell(new Phrase(med.getIdMedicamento().toString(), FONT_CELDA));
                table.addCell(new Phrase(med.getNombre(), FONT_CELDA));
                table.addCell(new Phrase(med.getPresentacion(), FONT_CELDA));
                table.addCell(new Phrase(med.getDescripcion() != null ? med.getDescripcion() : "", FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE RECETAS (VERSIÓN CORREGIDA SIN DETALLES DE MEDICAMENTOS) ---
    public ByteArrayInputStream generarReporteRecetas(List<Receta> recetas) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Reporte de Recetas Médicas", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            for (Receta receta : recetas) {
                // Usamos una tabla para organizar la información de cada receta
                PdfPTable tablaReceta = new PdfPTable(2);
                tablaReceta.setWidthPercentage(100);
                tablaReceta.setWidths(new float[]{1, 3}); // Columna de etiquetas más pequeña

                // Título para la receta
                PdfPCell cellTitulo = new PdfPCell(new Phrase("Detalles de la Receta ID: " + receta.getIdReceta(), FONT_HEADER_TABLA));
                cellTitulo.setColspan(2);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tablaReceta.addCell(cellTitulo);

                // Datos Generales
                tablaReceta.addCell(new Phrase("Fecha:", FONT_HEADER_TABLA));
                tablaReceta.addCell(new Phrase(receta.getFecha().toString(), FONT_CELDA));
                
                tablaReceta.addCell(new Phrase("Costo Consulta:", FONT_HEADER_TABLA));
                tablaReceta.addCell(new Phrase("$" + receta.getCostoConsulta().toString(), FONT_CELDA));

                // Datos del Doctor
                tablaReceta.addCell(new Phrase("Doctor:", FONT_HEADER_TABLA));
                String docInfo = receta.getDoctor().getUsuario().getNombre() + " " + receta.getDoctor().getUsuario().getApellidoPat() +
                                 "\nCédula: " + receta.getDoctor().getNoCedula() +
                                 "\nEspecialidad: " + receta.getDoctor().getEspecialidad().getNombre();
                tablaReceta.addCell(new Phrase(docInfo, FONT_CELDA));

                // Datos del Paciente
                tablaReceta.addCell(new Phrase("Paciente:", FONT_HEADER_TABLA));
                String pacInfo = receta.getPaciente().getUsuario().getNombre() + " " + receta.getPaciente().getUsuario().getApellidoPat() +
                                 "\nTipo Sangre: " + receta.getPaciente().getTipoSangre() +
                                 "\nAlergias: " + (receta.getPaciente().getAlergias() != null ? receta.getPaciente().getAlergias() : "Ninguna");
                tablaReceta.addCell(new Phrase(pacInfo, FONT_CELDA));
                
                document.add(tablaReceta);
                document.add(new Paragraph(" ")); // Espacio entre recetas
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE RECETA DETALLE ---
    public ByteArrayInputStream generarReporteRecetaDetalles(List<RecetaDetalle> detalles) {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Medicamentos Prescritos (Detalle General)", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6); // 6 columnas
            table.setWidthPercentage(100);
            Stream.of("ID Detalle", "ID Receta Padre", "Medicamento", "Dosis", "Cantidad", "Instrucciones")
                  .forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (RecetaDetalle detalle : detalles) {
                table.addCell(new Phrase(detalle.getIdRecetaDetalle().toString(), FONT_CELDA));
                table.addCell(new Phrase(detalle.getReceta().getIdReceta().toString(), FONT_CELDA));

                // Mostramos el nombre y presentación del medicamento
                String medInfo = detalle.getMedicamento().getNombre() + "\n(" + detalle.getMedicamento().getPresentacion() + ")";
                table.addCell(new Phrase(medInfo, FONT_CELDA));

                table.addCell(new Phrase(detalle.getDosificacion(), FONT_CELDA));
                table.addCell(new Phrase(detalle.getCantidad().toString(), FONT_CELDA));
                table.addCell(new Phrase(detalle.getInstrucciones() != null ? detalle.getInstrucciones() : "", FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODO PARA REPORTE DE CITAS (VERSIÓN FINAL Y COMPLETA) ---
    public ByteArrayInputStream generarReporteCitas(List<CitaConsulta> citas) {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para que quepa todo
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte General de Citas Médicas", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            // Creamos una tabla con 9 columnas
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 2, 3, 3, 3, 2, 3, 2}); // Ajustamos anchos
            Stream.of("ID Cita", "Fecha", "Hora", "Paciente", "Doctor", "Especialidad", "Consultorio", "Motivo", "Estatus")
                  .forEach(h -> table.addCell(crearCeldaHeader(h)));

            for (CitaConsulta cita : citas) {
                table.addCell(new Phrase(cita.getIdCita().toString(), FONT_CELDA));
                table.addCell(new Phrase(cita.getFecha().toString(), FONT_CELDA));
                table.addCell(new Phrase(cita.getHora().toString(), FONT_CELDA));
                
                String nombrePaciente = cita.getPaciente().getUsuario().getNombre() + " " + cita.getPaciente().getUsuario().getApellidoPat();
                table.addCell(new Phrase(nombrePaciente, FONT_CELDA));

                String nombreDoctor = cita.getDoctor().getUsuario().getNombre() + " " + cita.getDoctor().getUsuario().getApellidoPat();
                table.addCell(new Phrase(nombreDoctor, FONT_CELDA));

                table.addCell(new Phrase(cita.getEspecialidad().getNombre(), FONT_CELDA));
                table.addCell(new Phrase(cita.getConsultorio().getNumero(), FONT_CELDA));
                table.addCell(new Phrase(cita.getMotivo() != null ? cita.getMotivo() : "", FONT_CELDA));
                table.addCell(new Phrase(cita.getEstatus().getNombre(), FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

        // --- MÉTODO PARA REPORTE DE HISTORIAL CLÍNICO (VERSIÓN FINAL Y DETALLADA) ---
    public ByteArrayInputStream generarReporteHistoriales(List<HistorialClinico> historiales) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            // Título general del documento
            Paragraph tituloPrincipal = new Paragraph("Reporte de Historiales Clínicos", FONT_TITULO_SECCION);
            tituloPrincipal.setAlignment(Element.ALIGN_CENTER);
            document.add(tituloPrincipal);
            document.add(Chunk.NEWLINE);

            for (HistorialClinico historial : historiales) {
                // --- Encabezado de la Ficha Clínica ---
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{3, 2});
                
                PdfPCell tituloFicha = new PdfPCell(new Phrase("Ficha de Historial Clínico ID: " + historial.getIdHistorial(), FONT_HEADER_TABLA));
                tituloFicha.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(tituloFicha);
                
                PdfPCell fechaDiagCell = new PdfPCell(new Phrase("Fecha de Diagnóstico: " + historial.getFechaDiagnostico(), FONT_HEADER_TABLA));
                fechaDiagCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                fechaDiagCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(fechaDiagCell);
                document.add(headerTable);
                
                document.add(new Paragraph(" "));

                // --- Bloque de Información del Paciente y Doctor ---
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.addCell(crearCeldaInfoPaciente(historial.getPaciente()));
                infoTable.addCell(crearCeldaInfoDoctor(historial.getDoctor()));
                document.add(infoTable);
                document.add(new Paragraph(" "));

                // --- Bloque de Información de la Cita Asociada ---
                document.add(crearParrafoConTitulo("Detalles de la Cita Asociada (ID: " + historial.getCita().getIdCita() + ")"));
                String citaInfo = "Fecha y Hora: " + historial.getCita().getFecha() + " a las " + historial.getCita().getHora() +
                                  "\nMotivo de la Consulta: " + historial.getCita().getMotivo();
                document.add(crearParrafoContenido(citaInfo));
                
                // --- Bloque de Información Clínica ---
                document.add(crearParrafoConTitulo("Información Clínica"));
                String clinicaInfo = "Diagnóstico: " + historial.getDiagnostico() +
                                     "\nTratamiento Indicado: " + historial.getTratamiento() +
                                     "\nNotas de Evolución: " + (historial.getNotas() != null ? historial.getNotas() : "N/A");
                document.add(crearParrafoContenido(clinicaInfo));

                // --- Fecha de Alta (si existe) ---
                if (historial.getFechaAlta() != null) {
                    document.add(crearParrafoConTitulo("Fecha de Alta"));
                    document.add(crearParrafoContenido(historial.getFechaAlta().toString()));
                }

                if (historial.getArchivoAdjuntoPath() != null && !historial.getArchivoAdjuntoPath().isEmpty()) {
                    document.add(crearParrafoConTitulo("Archivo Adjunto"));
                    document.add(crearParrafoContenido("Nombre del archivo: " + historial.getArchivoAdjuntoPath()));
                }
                
                // Separador para la siguiente ficha, cada una en una nueva página
                document.newPage();
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- MÉTODOS AUXILIARES PARA ESTILO ---
    // (Puedes poner estos al final de tu clase PdfService)

    private PdfPCell crearCeldaInfoPaciente(Paciente paciente) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph("Datos del Paciente", FONT_HEADER_TABLA));
        // CORREGIDO: Se muestra el nombre completo
        String nombreCompleto = paciente.getUsuario().getNombre() + " " + paciente.getUsuario().getApellidoPat() + " " + (paciente.getUsuario().getApellidoMat() != null ? paciente.getUsuario().getApellidoMat() : "");
        cell.addElement(new Phrase("Nombre: " + nombreCompleto, FONT_CELDA));
        cell.addElement(new Phrase("\nEmail: " + paciente.getUsuario().getEmail(), FONT_CELDA));
        cell.addElement(new Phrase("\nTeléfono: " + paciente.getUsuario().getTelefono(), FONT_CELDA));
        cell.addElement(new Phrase("\nTipo de Sangre: " + paciente.getTipoSangre(), FONT_CELDA));
        cell.addElement(new Phrase("\nAlergias: " + (paciente.getAlergias() != null ? paciente.getAlergias() : "Ninguna"), FONT_CELDA));
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell crearCeldaInfoDoctor(Doctor doctor) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph("Médico Tratante", FONT_HEADER_TABLA));
        // CORREGIDO: Se muestra el nombre completo
        String nombreCompleto = doctor.getUsuario().getNombre() + " " + doctor.getUsuario().getApellidoPat() + " " + (doctor.getUsuario().getApellidoMat() != null ? doctor.getUsuario().getApellidoMat() : "");
        cell.addElement(new Phrase("Nombre: " + nombreCompleto, FONT_CELDA));
        cell.addElement(new Phrase("\nCédula Profesional: " + doctor.getNoCedula(), FONT_CELDA));
        cell.addElement(new Phrase("\nEspecialidad: " + doctor.getEspecialidad().getNombre(), FONT_CELDA));
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        return cell;
    }

    private Paragraph crearParrafoConTitulo(String texto) {
        Paragraph p = new Paragraph(texto, FONT_HEADER_TABLA);
        p.setSpacingBefore(10);
        return p;
    }

    private Paragraph crearParrafoContenido(String texto) {
        Paragraph p = new Paragraph(texto, FONT_CELDA);
        p.setIndentationLeft(10);
        p.setSpacingAfter(10);
        return p;
    }

        // --- MÉTODO PARA REPORTE DE RECEPCIONISTAS ---
    public ByteArrayInputStream generarReporteRecepcionistas(List<Recepcionista> recepcionistas) {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para más espacio
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Personal de Recepción", FONT_TITULO_SECCION));
            document.add(Chunk.NEWLINE);

            // Tabla con 8 columnas
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 2, 2, 3, 2, 2, 2}); // Ajustar anchos de columna
            
            Stream.of("ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono", "Consultorio", "Horario")
                  .forEach(h -> table.addCell(crearCeldaHeader(h)));
            
            for (Recepcionista recepcionista : recepcionistas) {
                // Datos del Recepcionista (ID)
                table.addCell(new Phrase(recepcionista.getIdRecepcionista().toString(), FONT_CELDA));

                // Datos del Usuario asociado
                Usuario usuario = recepcionista.getUsuario();
                table.addCell(new Phrase(usuario.getNombre(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getApellidoPat(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getApellidoMat() != null ? usuario.getApellidoMat() : "", FONT_CELDA));
                table.addCell(new Phrase(usuario.getEmail(), FONT_CELDA));
                table.addCell(new Phrase(usuario.getTelefono(), FONT_CELDA));
                
                // Datos del Consultorio asignado
                String consInfo = "N° " + recepcionista.getConsultorio().getNumero() + " (Piso " + recepcionista.getConsultorio().getPiso() + ")";
                table.addCell(new Phrase(consInfo, FONT_CELDA));

                // Datos del Horario asignado
                String horarioInfo = recepcionista.getHorario().getHoraInicio() + " - " + recepcionista.getHorario().getHoraFin();
                table.addCell(new Phrase(horarioInfo, FONT_CELDA));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }



    
    // ... (Aquí irían los otros métodos para generar los demás reportes, como el de Citas) ...


    // Método auxiliar para crear celdas de cabecera con estilo
    private PdfPCell crearCeldaHeader(String texto) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(texto, FONT_HEADER_TABLA));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        return header;
    }
}