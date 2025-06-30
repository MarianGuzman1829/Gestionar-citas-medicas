package com.hospital.servicios;

import com.hospital.dominio.entidades.DoctorConsultorio;
import com.hospital.dominio.entidades.DoctorConsultorioId;

import java.util.List;
import java.util.Optional;

public interface DoctorConsultorioService {
    // --- MÉTODOS QUE YA TENÍAS ---
    List<DoctorConsultorio> readAll();
    List<DoctorConsultorio> findByDoctorId(Long doctorId);
    List<DoctorConsultorio> findByConsultorioId(Long consultorioId);
    List<DoctorConsultorio> findByHorarioId(Long horarioId);
    DoctorConsultorio createAssignment(Long doctorId, Long consultorioId, Long horarioId);

    // --- NUEVOS MÉTODOS QUE DEBES AÑADIR ---

    /**
     * Busca una asignación por su clave primaria compuesta.
     * @param id La clave compuesta (DoctorConsultorioId).
     * @return Un Optional que contiene la asignación si se encuentra.
     */
    Optional<DoctorConsultorio> findById(DoctorConsultorioId id);

    /**
     * Elimina una asignación por su clave primaria compuesta.
     * @param id La clave compuesta (DoctorConsultorioId).
     */
    void deleteById(DoctorConsultorioId id);
    
    /**
     * Actualiza una asignación. Dado que la PK no puede cambiar, esto
     * generalmente significa borrar la antigua y crear una nueva.
     * @param oldId La clave de la asignación a reemplazar.
     * @param newDoctorId El ID del nuevo doctor.
     * @param newConsultorioId El ID del nuevo consultorio.
     * @param newHorarioId El ID del nuevo horario.
     * @return La nueva asignación creada.
     */
    DoctorConsultorio updateAssignment(DoctorConsultorioId oldId, Long newDoctorId, Long newConsultorioId, Long newHorarioId);
}