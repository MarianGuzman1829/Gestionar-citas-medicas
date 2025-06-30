package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Estatus;
import com.hospital.dominio.repositorios.EstatusRepository;
import com.hospital.servicios.EstatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class EstatusServiceImpl implements EstatusService {

    @Autowired
    private EstatusRepository estatusRepository;

    @Override
    public List<Estatus> readAll() {
        return estatusRepository.findAllByOrderByIdEstadoAsc();
    }

    @Override
    public Estatus read(Long id) {
        return estatusRepository.findById(id).orElse(null);
    }

    @Override
    public Estatus save(Estatus estatus) {
        return estatusRepository.save(estatus);
    }

    @Override
    public void delete(Long id) {
        estatusRepository.deleteById(id);
    }

    @Override
    public Optional<Estatus> findByNombre(String nombre) {
        return estatusRepository.findByNombre(nombre);
    }

    // Implementación del método obtenerEstatusPorId
    @Override
    public Estatus obtenerEstatusPorId(Long idEstado) {
        return estatusRepository.findById(idEstado).orElse(null);
    }
}
