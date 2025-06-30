package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    // Buscar un doctor por su número de cédula
    Optional<Doctor> findByNoCedula(String noCedula);

    // Buscar doctores por su especialidad
    List<Doctor> findByEspecialidad_Nombre(String nombre);

    // Buscar un doctor por su ID (esto ya está cubierto por JpaRepository.findById())
    Optional<Doctor> findByIdDoctor(Long idDoctor);

    List<Doctor> findAllByOrderByIdDoctorAsc();
}