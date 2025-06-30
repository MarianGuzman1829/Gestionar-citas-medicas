// src/main/java/com/hospital/servicios/impl/UsuarioServiceImpl.java
package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Usuario;
import com.hospital.dominio.repositorios.UsuarioRepository;
import com.hospital.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> readAll() {
        return usuarioRepository.findAllByOrderByIdUsuarioAsc();
    }

    @Override
    public Usuario read(Long id) {
        return usuarioRepository.findById(id).orElse(null); // Devuelve el usuario por ID
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Si la contraseña no está vacía, la encriptamos
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); // Encriptamos la contraseña
        }
        return usuarioRepository.save(usuario); // Guarda el usuario
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id); // Elimina el usuario por su ID
    }
}
