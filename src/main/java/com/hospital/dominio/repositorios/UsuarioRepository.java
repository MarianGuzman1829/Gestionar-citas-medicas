// src/main/java/com/hospital/dominio/repositorios/UsuarioRepository.java
package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Ejemplo de consulta personalizada:
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllByOrderByIdUsuarioAsc();
}
