package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.RecetaDetalle;
import com.hospital.dominio.repositorios.RecetaDetalleRepository;
import com.hospital.servicios.RecetaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecetaDetalleServiceImpl implements RecetaDetalleService {

    @Autowired
    private RecetaDetalleRepository recetaDetalleRepository;

    @Override
    public List<RecetaDetalle> getAllRecetasDetalles() {
        return recetaDetalleRepository.findAllByOrderByIdRecetaDetalleAsc();
    }

    @Override
    public List<RecetaDetalle> getRecetasDetallesByRecetaId(Long idReceta) {
        return recetaDetalleRepository.findByReceta_IdReceta(idReceta);
    }

    @Override
    public Optional<RecetaDetalle> getRecetaDetalleById(Long idRecetaDetalle) {
        return recetaDetalleRepository.findByIdRecetaDetalle(idRecetaDetalle);
    }

    @Override
    public RecetaDetalle saveRecetaDetalle(RecetaDetalle recetaDetalle) {
        return recetaDetalleRepository.save(recetaDetalle);
    }

    @Override
    public void deleteRecetaDetalle(Long idRecetaDetalle) {
        recetaDetalleRepository.deleteById(idRecetaDetalle);
    }
}
