package com.hospital.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // <-- ¡ESTE ES EL ÚNICO IMPORT DE NotNull QUE NECESITAS!
import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Doctor", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idDoctor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
        name = "doctor_id_seq",
        sequenceName = "doctor_iddoctor_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "doctor_id_seq"
    )
    @Column(name = "idDoctor")
    private Long idDoctor;

    @NotBlank(message = "El número de cédula es obligatorio")
    @Pattern(regexp = "^\\d{7,10}$", message = "El número de cédula debe contener entre 7 y 10 dígitos")
    @Column(name = "no_cedula", nullable = false, length = 50, unique = true)
    private String noCedula;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idEspecialidad", referencedColumnName = "idEspecialidad", nullable = false)
    private Especialidad especialidad;

    @NotNull(message = "El usuario asociado es obligatorio")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitaConsulta> citas;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistorialClinico> historiales;

    public Especialidad getEspecialidad() {
        return especialidad;
    }
}