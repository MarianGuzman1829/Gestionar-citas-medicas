package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Doctor_Consultorio", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Se agrega esta anotación para evitar ciclos recursivos
public class DoctorConsultorio implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @Valid
    private DoctorConsultorioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDoctor")
    @JoinColumn(name = "idDoctor", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idConsultorio")
    @JoinColumn(name = "idConsultorio", nullable = false)
    private Consultorio consultorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idHorario")
    @JoinColumn(name = "idHorario", nullable = false)
    private Horario horario;
}
