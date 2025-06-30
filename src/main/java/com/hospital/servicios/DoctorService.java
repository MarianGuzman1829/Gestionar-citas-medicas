package com.hospital.servicios;

import com.hospital.dominio.entidades.Doctor;
import java.util.List;

public interface DoctorService {
    List<Doctor> readAll();
    Doctor read(Long id);
    Doctor save(Doctor doctor);
    void delete(Long id);
    List<Doctor> findByEspecialidad(String especialidad);
    Doctor findByNoCedula(String noCedula);
    Doctor obtenerDoctorPorId(Long idDoctor);  // Nuevo método
}
