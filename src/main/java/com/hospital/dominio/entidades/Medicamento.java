package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Medicamento", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "medicamento_id_seq", sequenceName = "medicamento_idmedicamento_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicamento_id_seq")
    @Column(name = "idMedicamento")
    private Long idMedicamento;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Pattern(
     regexp = "^[\\p{L}\\d\\s\\-]+$",
     message = "El nombre debe contener solo letras, números, espacios o guiones")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La presentación es obligatoria")
    @Size(max = 100, message = "La presentación no debe exceder 100 caracteres")
    @Column(name = "presentacion", nullable = false, length = 100)
    private String presentacion;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //(value = "medicamentoReference")
    private List<RecetaDetalle> recetaDetalles;


}