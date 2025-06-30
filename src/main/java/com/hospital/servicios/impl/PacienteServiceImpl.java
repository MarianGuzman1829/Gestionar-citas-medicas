package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Paciente;
import com.hospital.dominio.repositorios.PacienteRepository;
import com.hospital.servicios.PacienteService;
import com.hospital.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public List<Paciente> readAll() {
        return pacienteRepository.findAllByOrderByIdPacienteAsc();
    }

    @Override
    public Paciente read(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    @Override
    public Paciente save(Paciente paciente) {
        if (paciente.getUsuario() != null && paciente.getUsuario().getContrasena() != null) {
            usuarioService.save(paciente.getUsuario());
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    public void delete(Long id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public Paciente findByCurp(String curp) {
        return pacienteRepository.findByCurp(curp).orElse(null);
    }

    // Implementación del método obtenerPacientePorId
    @Override
    public Paciente obtenerPacientePorId(Long idPaciente) {
        return pacienteRepository.findById(idPaciente).orElse(null);
    }
}
