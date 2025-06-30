package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Consultorio;
import com.hospital.dominio.repositorios.ConsultorioRepository;
import com.hospital.servicios.ConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultorioServiceImpl implements ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Override
    public List<Consultorio> readAll() {
        return consultorioRepository.findAllByOrderByIdConsultorioAsc();
    }
    
    @Override
    public Consultorio read(Long id) {
        return consultorioRepository.findById(id).orElse(null);
    }

    @Override
    public Consultorio save(Consultorio consultorio) {
        return consultorioRepository.save(consultorio);
    }

    @Override
    public void delete(Long id) {
        consultorioRepository.deleteById(id);
    }

    @Override
    public Optional<Consultorio> findByNumero(String numero) {
        return consultorioRepository.findByNumero(numero);
    }

    @Override
    public List<Consultorio> findByEstatus(Long estatusId) {
        return consultorioRepository.findByEstatus_IdEstado(estatusId);
    }

    @Override
    public List<Consultorio> findByPiso(int piso) {
        return consultorioRepository.findByPiso(piso);  
    }

    // Implementación del método obtenerConsultorioPorId
    @Override
    public Consultorio obtenerConsultorioPorId(Long idConsultorio) {
        return consultorioRepository.findById(idConsultorio).orElse(null);
    }
}
