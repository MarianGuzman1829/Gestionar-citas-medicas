package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.TipoUsuario;
import com.hospital.dominio.repositorios.TipoUsuarioRepository;
import com.hospital.servicios.TipoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Override
    public List<TipoUsuario> readAll() {
        return tipoUsuarioRepository.findAllByOrderByIdTipoUsuarioAsc();
    }

    @Override
    public TipoUsuario read(Long id) {
        return tipoUsuarioRepository.findById(id).orElse(null);
    }

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    @Override
    public void delete(Long id) {
        tipoUsuarioRepository.deleteById(id);
    }

    @Override
    public TipoUsuario findByNombre(String nombre) {
        // Llama al método correspondiente en el repositorio
        return tipoUsuarioRepository.findByNombre(nombre);
    }
}