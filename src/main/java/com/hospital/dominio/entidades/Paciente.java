package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Paciente", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPaciente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator( // <-- AÑADIDO: Define un generador de secuencia propio para Paciente.
        name = "paciente_id_seq",
        sequenceName = "paciente_idpaciente_seq",
        allocationSize = 1
    )
    @GeneratedValue( // <-- AÑADIDO: Le dice a JPA que autogenere el ID usando la secuencia de arriba.
        strategy = GenerationType.SEQUENCE,
        generator = "paciente_id_seq"
    )
    @Column(name = "idPaciente")
    private Long idPaciente;

    @NotBlank(message = "La CURP es obligatoria")
    @Size(min = 18, max = 18, message = "La CURP debe tener 18 caracteres")
    @Pattern(regexp = "^[A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d]\\d$",
         message = "La CURP no cumple con el formato estándar de 18 caracteres") 
    @Column(name = "curp", nullable = false, length = 18, unique = true)
    private String curp;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaNacimiento", nullable = false)
    private Date fechaNacimiento;

    @NotBlank(message = "El tipo de sangre es obligatorio")
    @Size(max = 5, message = "El tipo de sangre no debe exceder 5 caracteres")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido (ej: A+, O-, AB+)")
    @Column(name = "tipoSangre", nullable = false, length = 5)
    private String tipoSangre;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    // Relación con Usuario
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // <-- MODIFICADO: Se mantiene la relación uno a uno.
    // @MapsId // <-- ELIMINADO: Esta era la línea que forzaba a los IDs a ser iguales.
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario", unique = true) // <-- MODIFICADO: La relación ahora se basa en una nueva columna "usuario_id", que debe ser única.
    private Usuario usuario;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitaConsulta> citas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHistorial", referencedColumnName = "idHistorial")
    private HistorialClinico historialClinico;
}
