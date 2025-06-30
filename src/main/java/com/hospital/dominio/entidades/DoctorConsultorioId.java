package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class DoctorConsultorioId implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "El idDoctor es obligatorio")
    @Column(name = "idDoctor")
    private Long idDoctor;

    @NotNull(message = "El idConsultorio es obligatorio")
    @Column(name = "idConsultorio")
    private Long idConsultorio;

    @NotNull(message = "El idHorario es obligatorio")
    @Column(name = "idHorario")
    private Long idHorario;
}