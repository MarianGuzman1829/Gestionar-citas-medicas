package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.DoctorConsultorio;
import com.hospital.dominio.entidades.DoctorConsultorioId;
import com.hospital.dominio.entidades.Doctor;
import com.hospital.dominio.entidades.Consultorio;
import com.hospital.dominio.entidades.Horario;
import com.hospital.dominio.repositorios.DoctorConsultorioRepository; // Cambiado desde dominio.repositorios
import com.hospital.dominio.repositorios.DoctorRepository;        // Cambiado desde dominio.repositorios
import com.hospital.dominio.repositorios.ConsultorioRepository;   // Cambiado desde dominio.repositorios
import com.hospital.dominio.repositorios.HorarioRepository;       // Cambiado desde dominio.repositorios
import com.hospital.servicios.DoctorConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorConsultorioServiceImpl implements DoctorConsultorioService {

    @Autowired
    private DoctorConsultorioRepository dcRepo;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    // --- MÉTODOS QUE YA TENÍAS ---
    @Override
    public List<DoctorConsultorio> readAll() {
        return dcRepo.findAll();
    }

    @Override
    public List<DoctorConsultorio> findByDoctorId(Long doctorId) {
        return dcRepo.findByDoctor_IdDoctor(doctorId);
    }

    @Override
    public List<DoctorConsultorio> findByConsultorioId(Long consultorioId) {
        return dcRepo.findByConsultorio_IdConsultorio(consultorioId);
    }

    @Override
    public List<DoctorConsultorio> findByHorarioId(Long horarioId) {
        return dcRepo.findByHorario_IdHorario(horarioId);
    }

    @Override
    @Transactional
    public DoctorConsultorio createAssignment(Long doctorId, Long consultorioId, Long horarioId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        Consultorio consultorio = consultorioRepository.findById(consultorioId)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        DoctorConsultorioId id = new DoctorConsultorioId(doctorId, consultorioId, horarioId);

        DoctorConsultorio asignacion = new DoctorConsultorio();
        asignacion.setId(id);
        asignacion.setDoctor(doctor);
        asignacion.setConsultorio(consultorio);
        asignacion.setHorario(horario);

        return dcRepo.save(asignacion);
    }

    // --- IMPLEMENTACIÓN DE LOS NUEVOS MÉTODOS ---

    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorConsultorio> findById(DoctorConsultorioId id) {
        return dcRepo.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(DoctorConsultorioId id) {
        // El repositorio se encargará de lanzar una excepción si no existe,
        // pero el controlador ya lo verifica.
        dcRepo.deleteById(id);
    }

    @Override
    @Transactional
    public DoctorConsultorio updateAssignment(DoctorConsultorioId oldId, Long newDoctorId, Long newConsultorioId, Long newHorarioId) {
        // Para una clave compuesta, "actualizar" realmente significa borrar la asignación vieja y crear una nueva.
        // Esto previene inconsistencias y es el patrón más seguro.
        
        // 1. Verificamos que la asignación a reemplazar exista.
        dcRepo.findById(oldId)
                .orElseThrow(() -> new RuntimeException("La asignación que intentas actualizar no existe."));

        // 2. Borramos la asignación vieja.
        dcRepo.deleteById(oldId);

        // 3. Creamos la nueva asignación llamando a nuestro propio método `createAssignment`.
        return createAssignment(newDoctorId, newConsultorioId, newHorarioId);
    }
}