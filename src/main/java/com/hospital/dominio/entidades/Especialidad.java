package com.hospital.dominio.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "Especialidad", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Especialidad {

    @Id
    @SequenceGenerator(name = "especialidad_id_seq", sequenceName = "especialidad_idespecialidad_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especialidad_id_seq")
    @Column(name = "idEspecialidad")
    private Long idEspecialidad;

    @NotBlank(message = "El nombre de la especialidad no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitaConsulta> citas;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    @JsonProperty("doctors")
    public List<Long> getDoctorsForResponse() {
        return doctors != null ? doctors.stream()
                .map(Doctor::getIdDoctor)
                .collect(Collectors.toList()) : null;
    }
}
