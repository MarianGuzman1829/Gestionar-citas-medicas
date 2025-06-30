package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Doctor;
import com.hospital.dominio.entidades.Especialidad;
import com.hospital.dominio.repositorios.DoctorRepository;
import com.hospital.servicios.DoctorService;
import com.hospital.servicios.EspecialidadService;
import com.hospital.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspecialidadService especialidadService;

    @Override
    public List<Doctor> readAll() {
        return doctorRepository.findAllByOrderByIdDoctorAsc();
    }

    @Override
    public Doctor read(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor.getUsuario() != null) {
            usuarioService.save(doctor.getUsuario());
        }

        Especialidad especialidad = especialidadService.findByNombre(doctor.getEspecialidad().getNombre());
        if (especialidad == null) {
            especialidad = especialidadService.save(doctor.getEspecialidad());
        }

        doctor.setEspecialidad(especialidad);

        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Doctor> findByEspecialidad(String especialidad) {
        return doctorRepository.findByEspecialidad_Nombre(especialidad);
    }

    @Override
    public Doctor findByNoCedula(String noCedula) {
        return doctorRepository.findByNoCedula(noCedula).orElse(null);
    }

    // Implementación del método obtenerDoctorPorId
    @Override
    public Doctor obtenerDoctorPorId(Long idDoctor) {
        return doctorRepository.findById(idDoctor).orElse(null);
    }
}
