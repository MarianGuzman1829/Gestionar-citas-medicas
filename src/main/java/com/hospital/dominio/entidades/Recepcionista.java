package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Recepcionista", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idRecepcionista")
public class Recepcionista implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator( // <-- AÑADIDO: Define un generador de secuencia propio para Recepcionista.
        name = "recepcionista_id_seq",
        sequenceName = "recepcionista_idrecepcionista_seq",
        allocationSize = 1
    )
    @GeneratedValue( // <-- AÑADIDO: Le dice a JPA que autogenere el ID usando la secuencia de arriba.
        strategy = GenerationType.SEQUENCE,
        generator = "recepcionista_id_seq"
    )
    @Column(name = "idRecepcionista", nullable = false)
    private Long idRecepcionista;

    @NotNull(message = "El usuario es obligatorio")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario", unique = true) 
    private Usuario usuario;

    @NotNull(message = "El consultorio es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idConsultorio", nullable = false)
    private Consultorio consultorio;

    @NotNull(message = "El horario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHorario", referencedColumnName = "idHorario", nullable = false)
    private Horario horario;
}
