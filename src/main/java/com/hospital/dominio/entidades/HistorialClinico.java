package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Historial_Clinico", schema = "public")
public class HistorialClinico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "historial_id_seq", sequenceName = "historial_clinico_idhistorial_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_id_seq")
    @Column(name = "idHistorial", nullable = false)
    private Long idHistorial;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Column(name = "diagnostico", nullable = false, length = 100)
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Column(name = "tratamiento", nullable = false, columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @NotNull(message = "La fecha de diagnóstico es obligatoria")
    @PastOrPresent(message = "La fecha de diagnóstico no puede ser futura")
    @Column(name = "fecha_diagnostico", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDiagnostico;

    @PastOrPresent(message = "La fecha de alta no puede ser futura")
    @Column(name = "fecha_alta")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaAlta;

    // Añadimos campos para el archivo ---
    @Column(name = "archivo_adjunto_path")
    private String archivoAdjuntoPath; // Guarda el nombre del archivo en el servidor

    @Column(name = "archivo_adjunto_data", insertable = false, updatable = true)
    private byte[] archivoAdjuntoData;

    @NotNull(message = "El doctor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor", nullable = false)
    //(value = "doctor") // Nombre único para evitar conflictos
    private Doctor doctor;

    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente", nullable = false)
    //(value = "paciente") // Nombre único para evitar conflictos
    private Paciente paciente;

    @NotNull(message = "La cita es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCita", referencedColumnName = "idCita", nullable = false)
    //(value = "cita") // Nombre único para evitar conflictos
    private CitaConsulta cita;

    @NotNull(message = "La receta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReceta", referencedColumnName = "idReceta", nullable = false)
    //(value = "receta") // Nombre único para evitar conflictos
    private Receta receta;

    // Métodos personalizados para obtener detalles del doctor
    @JsonProperty("doctor")
    public Map<String, String> getDoctorDetails() {
        Map<String, String> doctorDetails = new LinkedHashMap<>();
            if (doctor != null) {
            doctorDetails.put("nombre", doctor.getUsuario() != null ? doctor.getUsuario().getNombre() : "N/A");
            doctorDetails.put("apellido", doctor.getUsuario() != null ? doctor.getUsuario().getApellidoPat() : "N/A");
            doctorDetails.put("noCedula", doctor.getNoCedula());
            doctorDetails.put("especialidad", doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombre() : "N/A");
        }
        return doctorDetails;
    }


// Métodos personalizados para obtener detalles del paciente
@JsonProperty("paciente")
public Map<String, String> getPacienteDetails() {
    Map<String, String> pacienteDetails = new LinkedHashMap<>();
    if (paciente != null && paciente.getUsuario() != null) {
        pacienteDetails.put("nombre", paciente.getUsuario().getNombre());
        pacienteDetails.put("apellidoPat", paciente.getUsuario().getApellidoPat());
        pacienteDetails.put("apellidoMat", paciente.getUsuario().getApellidoMat());
        pacienteDetails.put("curp", paciente.getCurp());
    }
    return pacienteDetails;
}

// Métodos personalizados para obtener detalles de la cita
@JsonProperty("cita")
public Map<String, String> getCitaDetails() {
    Map<String, String> citaDetails = new LinkedHashMap<>();
    if (cita != null) {
        citaDetails.put("fecha", cita.getFecha() != null ? cita.getFecha().toString() : "N/A");
        citaDetails.put("hora", cita.getHora() != null ? cita.getHora().toString() : "N/A");
        citaDetails.put("motivo", cita.getMotivo());
    }
    return citaDetails;
}

// Métodos personalizados para obtener detalles de la receta y el medicamento
@JsonProperty("receta")
public Map<String, Object> getRecetaDetails() {
    Map<String, Object> recetaDetails = new LinkedHashMap<>();
    if (receta != null) {
        recetaDetails.put("fecha", receta.getFecha() != null ? receta.getFecha().toString() : "N/A");
        if (receta.getRecetaDetalles() != null && !receta.getRecetaDetalles().isEmpty()) {
            List<String> medicamentos = receta.getRecetaDetalles().stream()
                .map(rd -> rd.getMedicamentoNombre())
                .collect(Collectors.toList());
            recetaDetails.put("medicamentos", medicamentos);
        }
    }
    return recetaDetails;
}
}

