package com.hospital.servicios;

import com.hospital.dominio.entidades.TipoUsuario;
import java.util.List;

public interface TipoUsuarioService {
    List<TipoUsuario> readAll();
    TipoUsuario read(Long id);
    TipoUsuario save(TipoUsuario tipoUsuario);
    void delete(Long id);
    TipoUsuario findByNombre(String nombre);
}