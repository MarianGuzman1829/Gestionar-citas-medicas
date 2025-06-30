package com.hospital.dominio.entidades;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Horario", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idHorario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Horario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "horario_id_seq", sequenceName = "horario_idhorario_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horario_id_seq")
    @Column(name = "idHorario", nullable = false)
    private Long idHorario;

    // Para la consulta, se espera una hora específica (obligatorio)
    @Column(name = "horario", nullable = true) // Obligatorio para consulta
    private LocalTime horario;  // Obligatorio para consulta
    
    // Para el consultorio, se requiere hora de inicio y fin (obligatorio)
    @Column(name = "hora_inicio", nullable = true)  // Cambiado a nullable = true
    private LocalTime horaInicio;  // Obligatorio para consultorio
    
    @Column(name = "hora_fin", nullable = true)  // Cambiado a nullable = true
    private LocalTime horaFin;  // Obligatorio para consultorio

    @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorConsultorio> doctorConsultorios;

    
    @OneToMany(mappedBy = "horario", fetch = FetchType.LAZY)
    //(value = "recepcionista-horario") // Asegúrate de que coincida con el nombre único de la referencia
    private List<Recepcionista> recepcionistas;

    // Validación adicional para que horaFin sea posterior a horaInicio
    @AssertTrue(message = "La hora de fin debe ser posterior a la hora de inicio")
    public boolean isHoraFinValida() {
        if (horaInicio != null && horaFin != null) {
            return horaFin.isAfter(horaInicio);  // Verifica que horaFin sea después de horaInicio
        }
        return true; // Si alguna de las dos es null, no hacemos validación
    }
}
