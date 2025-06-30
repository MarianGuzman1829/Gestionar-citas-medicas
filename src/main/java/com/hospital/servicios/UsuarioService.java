package com.hospital.servicios;

import com.hospital.dominio.entidades.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> readAll();
    Usuario read(Long id);
    Usuario save(Usuario usuario);
    void delete(Long id);
}