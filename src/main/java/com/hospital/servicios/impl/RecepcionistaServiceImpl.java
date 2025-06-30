package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Recepcionista;
import com.hospital.dominio.repositorios.RecepcionistaRepository;
import com.hospital.servicios.RecepcionistaService;
import com.hospital.servicios.UsuarioService;
import com.hospital.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecepcionistaServiceImpl implements RecepcionistaService {

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public List<Recepcionista> obtenerTodosLosRecepcionistas() {
        return recepcionistaRepository.findAllByOrderByIdRecepcionistaAsc();
    }

    @Override
    public Optional<Recepcionista> obtenerRecepcionistaPorId(Long idRecepcionista) {
        return recepcionistaRepository.findById(idRecepcionista);
    }

    @Override
    public Recepcionista crearRecepcionista(Recepcionista recepcionista) {
        // Verificar si el Usuario ya existe
        if (recepcionista.getUsuario() != null) {
            // Si el Usuario ya existe, lo asociamos con el Recepcionista sin intentar crear uno nuevo
            Usuario usuarioExistente = usuarioService.read(recepcionista.getUsuario().getIdUsuario());
            if (usuarioExistente != null) {
                recepcionista.setUsuario(usuarioExistente);
            } else {
                // Si el Usuario no existe, lo persistimos
                recepcionista.setUsuario(usuarioService.save(recepcionista.getUsuario()));
            }
        }

        // Persistir el Recepcionista
        return recepcionistaRepository.save(recepcionista);
    }

    @Override
    public Recepcionista actualizarRecepcionista(Recepcionista recepcionista) {
        return recepcionistaRepository.save(recepcionista);
    }

    @Override
    public void eliminarRecepcionista(Long idRecepcionista) {
        recepcionistaRepository.deleteById(idRecepcionista);
    }
}
