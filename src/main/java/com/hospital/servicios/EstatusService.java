package com.hospital.servicios;

import com.hospital.dominio.entidades.Estatus;
import java.util.List;
import java.util.Optional;

public interface EstatusService {
    List<Estatus> readAll();
    Estatus read(Long id);
    Estatus save(Estatus estatus);
    void delete(Long id);
    Optional<Estatus> findByNombre(String nombre);
    Estatus obtenerEstatusPorId(Long idEstado);  // Nuevo método
}
