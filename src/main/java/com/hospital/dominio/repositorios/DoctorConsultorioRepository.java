package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.DoctorConsultorio;
import com.hospital.dominio.entidades.DoctorConsultorioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorConsultorioRepository extends JpaRepository<DoctorConsultorio, DoctorConsultorioId> {
    // Listar todas las asignaciones de un doctor por su ID
    List<DoctorConsultorio> findByDoctor_IdDoctor(Long idDoctor);

    // Listar todas las asignaciones de un consultorio por su ID
    List<DoctorConsultorio> findByConsultorio_IdConsultorio(Long idConsultorio);

    // Listar todas las asignaciones para un horario en particular
    List<DoctorConsultorio> findByHorario_IdHorario(Long idHorario);
}