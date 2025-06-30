package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Cita_Consulta", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCita")
@JsonInclude(JsonInclude.Include.NON_NULL) // Evita la serialización de valores null
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class CitaConsulta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "cita_consulta_id_seq", sequenceName = "cita_consulta_idcita_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cita_consulta_id_seq")
    @Column(name = "idCita", nullable = false)
    private Long idCita;

    @NotNull(message = "El doctor es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor", nullable = false)
    //(value = "doctor-citas")  // Relación inversa
    private Doctor doctor;
    
    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente", nullable = false)
    //(value = "paciente-citas") // Cambiar a JsonBackReference
    private Paciente paciente;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha", nullable = false)
    @JsonProperty("fecha")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "La hora de la cita es obligatoria")
    @Column(name = "hora", nullable = false)
    private java.time.LocalTime hora;

    @NotNull(message = "El estatus es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEstado", referencedColumnName = "idEstado", nullable = false)
    //(value = "estatus-citas")
    private Estatus estatus;

    @NotNull(message = "El consultorio es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idConsultorio", referencedColumnName = "idConsultorio", nullable = false)
    //(value = "consultorio-citas")  // Esto está correcto
    private Consultorio consultorio;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEspecialidad", referencedColumnName = "idEspecialidad", nullable = false)
    //(value = "especialidad-citas")
    private Especialidad especialidad;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHistorial", referencedColumnName = "idHistorial")
    //(value = "cita") // Asegurarse de que coincida con la // en HistorialClinico
    private HistorialClinico historialClinico;


    @JsonProperty("doctor")
    public Map<String, Object> getDoctorDetails() {
        if (doctor != null) {
            Map<String, Object> doctorDetails = new LinkedHashMap<>();
            doctorDetails.put("nombre", doctor.getUsuario().getNombre());
            doctorDetails.put("apellido", doctor.getUsuario().getApellidoPat());
            doctorDetails.put("noCedula", doctor.getNoCedula());
            doctorDetails.put("especialidad", doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombre() : "N/A");
            return doctorDetails;
        }
        return null;
    }

    @JsonProperty("paciente")
    public Map<String, Object> getPacienteDetails() {
        if (paciente != null && paciente.getUsuario() != null) {
            Map<String, Object> pacienteDetails = new LinkedHashMap<>();
            pacienteDetails.put("curp", paciente.getCurp());
            pacienteDetails.put("nombre", paciente.getUsuario().getNombre());
            pacienteDetails.put("apellidoPat", paciente.getUsuario().getApellidoPat());
            pacienteDetails.put("apellidoMat", paciente.getUsuario().getApellidoMat());
            return pacienteDetails;
        }
        return null;
    }

    @JsonProperty("estatus")
    public String getEstatusDetails() {
        return estatus != null ? estatus.getNombre() : "N/A";
    }

    @JsonProperty("consultorio")
    public String getConsultorioDetails() {
        return consultorio != null ? consultorio.getNumero() : "N/A";
    }

    // Este método transformará la entidad en la estructura que necesitas para la respuesta
    public Map<String, Object> toResponseMap() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("idCita", this.idCita);
        // Convertir LocalDate a String con el formato adecuado
        response.put("fecha", this.fecha != null ? this.fecha.toString() : null);
        response.put("hora", this.hora != null ? Arrays.asList(this.hora.getHour(), this.hora.getMinute()) : null);
        response.put("motivo", this.motivo);
        response.put("doctor", getDoctorDetails());
        response.put("paciente", getPacienteDetails());
        response.put("estatus", getEstatusDetails());
        response.put("consultorio", getConsultorioDetails());
        return response;
    }
}
