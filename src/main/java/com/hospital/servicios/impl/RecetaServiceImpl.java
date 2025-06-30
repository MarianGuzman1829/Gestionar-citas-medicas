package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Receta;
import com.hospital.dominio.repositorios.RecetaRepository;
import com.hospital.servicios.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Override
    public List<Receta> readAll() {
        return recetaRepository.findAllByOrderByIdRecetaAsc();
    }

    @Override
    public Receta read(Long id) {
        return recetaRepository.findByIdReceta(id).orElse(null); // Obtener receta por ID
    }

    @Override
    public Receta save(Receta receta) {
        return recetaRepository.save(receta); // Guardar o actualizar receta
    }

    @Override
    public void delete(Long id) {
        recetaRepository.deleteById(id); // Eliminar receta por ID
    }

    @Override
    public List<Receta> findByPaciente(Long idPaciente) {
        return recetaRepository.findByPaciente_IdPaciente(idPaciente); // Buscar recetas por paciente
    }

    @Override
    public List<Receta> findByDoctor(Long idDoctor) {
        return recetaRepository.findByDoctor_IdDoctor(idDoctor); // Buscar recetas por doctor
    }
}
