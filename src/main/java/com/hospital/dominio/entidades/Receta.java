package com.hospital.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Collections;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Receta", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idReceta") // Identidad única
public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "receta_id_seq", sequenceName = "receta_idreceta_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta_id_seq")
    @Basic(optional = false)
    @Column(name = "idReceta")
    private Long idReceta;

    @NotNull(message = "El costo de la consulta es obligatorio")
    @Positive(message = "El costo debe ser un valor positivo")
    @Column(name = "costo_consulta", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoConsulta;

    @NotNull(message = "La fecha de la receta es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor", nullable = false)
    private Doctor doctor;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente", nullable = false)
    private Paciente paciente;
    
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //(value = "recetaDetallesReference")
    private List<RecetaDetalle> recetaDetalles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHistorial", referencedColumnName = "idHistorial")
    //(value = "receta") // Asegurarse de que coincida con la // en HistorialClinico
    private HistorialClinico historialClinico;

    // Método getter para obtener el costo de la consulta
    @JsonProperty("medicamentos")
    public List<String> getMedicamentos() {
        if (recetaDetalles != null) {
            return recetaDetalles.stream()
                .map(rd -> rd.getMedicamentoNombre())
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    // Método getter para obtener los detalles del Doctor (como Map)
    @JsonProperty("doctor")
    @JsonPropertyOrder({"nombre", "noCedula", "especialidad"})
    public Map<String, Object> getDoctorDetails() {
        if (doctor != null) {
            Map<String, Object> doctorDetails = new HashMap<>();
            doctorDetails.put("nombre", doctor.getUsuario().getNombre() != null ? doctor.getUsuario().getNombre() : "N/A");
            doctorDetails.put("noCedula", doctor.getNoCedula());
            doctorDetails.put("especialidad", doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombre() : "N/A");
            return doctorDetails;
        }
        return null;
    }

    // Método getter para obtener los detalles del Paciente (como Map)
    @JsonProperty("paciente")
    public Map<String, Object> getPacienteDetails() {
        if (paciente != null && paciente.getUsuario() != null) {
            Map<String, Object> pacienteDetails = new LinkedHashMap<>();  // Usamos LinkedHashMap para preservar el orden
            pacienteDetails.put("curp", paciente.getCurp());
            pacienteDetails.put("nombre", paciente.getUsuario().getNombre() != null ? paciente.getUsuario().getNombre() : "N/A");
            pacienteDetails.put("apellidoPat", paciente.getUsuario().getApellidoPat() != null ? paciente.getUsuario().getApellidoPat() : "N/A");
            pacienteDetails.put("apellidoMat", paciente.getUsuario().getApellidoMat() != null ? paciente.getUsuario().getApellidoMat() : "N/A");
            return pacienteDetails;
        }
    return null;
    }
}
