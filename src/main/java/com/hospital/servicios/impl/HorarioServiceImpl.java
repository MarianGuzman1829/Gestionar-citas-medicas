package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Horario;
import com.hospital.dominio.repositorios.HorarioRepository;
import com.hospital.servicios.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Horario> readAll() {
        return horarioRepository.findAllByOrderByIdHorarioAsc();
    }

    @Override
    public Horario read(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }

    @Override
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public void delete(Long id) {
        horarioRepository.deleteById(id);
    }
}