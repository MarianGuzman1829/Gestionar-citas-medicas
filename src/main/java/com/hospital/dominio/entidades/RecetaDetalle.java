package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Receta_Detalle", schema = "public")
public class RecetaDetalle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "receta_detalle_id_seq", sequenceName = "receta_detalle_idrecetadetalle_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta_detalle_id_seq")
    @Basic(optional = false)
    @Column(name = "idRecetaDetalle")
    private Long idRecetaDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReceta", referencedColumnName = "idReceta", nullable = false)
    //(value = "recetaDetallesReference")
    private Receta receta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMedicamento", referencedColumnName = "idMedicamento", nullable = false)
    //(value = "medicamentoReference")  
    private Medicamento medicamento;

    @NotBlank(message = "La dosificación es obligatoria")
    @Column(name = "dosificacion", nullable = false, length = 100)
    private String dosificacion;

    @Positive(message = "La cantidad debe ser un número positivo")
    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Size(max = 500, message = "Las instrucciones no deben exceder 500 caracteres")
    @Column(name = "instrucciones", columnDefinition = "TEXT")
    private String instrucciones;

    // Método getter para obtener el medicamento y la fecha de la receta
    @JsonProperty("medicamento")
    public String getMedicamentoNombre() {
        if (medicamento != null) {
            return medicamento.getNombre();  // Devuelve el nombre del medicamento
        }
        return "N/A";
    }

    @JsonProperty("fechaReceta")
    public String getFechaReceta() {
        if (receta != null) {
            return receta.getFecha() != null ? receta.getFecha().toString() : "N/A"; // Devuelve la fecha de la receta
        }
        return "N/A";
    }
}