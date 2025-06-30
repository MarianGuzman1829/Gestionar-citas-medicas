package com.hospital.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Estatus", schema = "public")
public class Estatus {
    @Id
    @SequenceGenerator(name = "estatus_id_seq", sequenceName = "estatus_idestado_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estatus_id_seq")
    @Column(name = "idEstado")
    private Long idEstado;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "estatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitaConsulta> citas;
}