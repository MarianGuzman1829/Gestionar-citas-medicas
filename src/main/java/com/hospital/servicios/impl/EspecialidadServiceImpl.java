package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Especialidad;
import com.hospital.dominio.repositorios.EspecialidadRepository;
import com.hospital.servicios.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> readAll() {
        return especialidadRepository.findAllByOrderByIdEspecialidadAsc();
    }   

    @Override
    public Especialidad read(Long id) {
        return especialidadRepository.findById(id).orElse(null);
    }

    @Override
    public Especialidad save(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void delete(Long id) {
        especialidadRepository.deleteById(id);
    }

    @Override
    public Especialidad findByNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre).orElse(null);
    }

    // Implementación del método obtenerEspecialidadPorId
    @Override
    public Especialidad obtenerEspecialidadPorId(Long idEspecialidad) {
        return especialidadRepository.findById(idEspecialidad).orElse(null);
    }
}
