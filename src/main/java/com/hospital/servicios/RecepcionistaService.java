package com.hospital.servicios;

import com.hospital.dominio.entidades.Recepcionista;

import java.util.List;
import java.util.Optional;

public interface RecepcionistaService {
    List<Recepcionista> obtenerTodosLosRecepcionistas();
    Optional<Recepcionista> obtenerRecepcionistaPorId(Long idRecepcionista);
    Recepcionista crearRecepcionista(Recepcionista recepcionista);
    Recepcionista actualizarRecepcionista(Recepcionista recepcionista);
    void eliminarRecepcionista(Long idRecepcionista);
}
