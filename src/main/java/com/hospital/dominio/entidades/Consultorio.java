package com.hospital.dominio.entidades;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Consultorio", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Consultorio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "consultorio_id_seq", sequenceName = "consultorio_idconsultorio_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultorio_id_seq")
    @Basic(optional=false)
    @Column(name="idConsultorio")
    private Long idConsultorio;

    @NotBlank(message="El número de consultorio es obligatorio")
    @Column(name="numero", nullable=false, length=10, unique=true)
    private String numero;

    @NotNull(message = "El estado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idEstado", referencedColumnName="idEstado", nullable=false)
    //
    private Estatus estatus;

    @NotNull(message = "El piso es obligatorio")
    @Column(name="piso", nullable=false)
    private Integer piso;

    @NotNull(message = "La descripción es obligatoria")
    @Column(name="descripcion", length=100)
    private String descripcion;

    // Relación inversa para 'consultorio-citas'
    @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //(value = "consultorio-citas")
    private List<CitaConsulta> citas;

    // Relación inversa para 'consultorio-doctor'
    //(value = "recepcionista-consultorio")
    @OneToMany(mappedBy = "consultorio", fetch = FetchType.LAZY)
    private List<Recepcionista> recepcionistas;
}
