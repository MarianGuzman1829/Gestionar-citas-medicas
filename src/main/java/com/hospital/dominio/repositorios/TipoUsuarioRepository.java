// src/main/java/com/hospital/dominio/repositorios/TipoUsuarioRepository.java
package com.hospital.dominio.repositorios;

import com.hospital.dominio.entidades.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    // Buscar un tipo de usuario por su nombre
    TipoUsuario findByNombre(String nombre);

    // Buscar un tipo de usuario por su ID
    Optional<TipoUsuario> findByIdTipoUsuario(Long idTipoUsuario);

    List<TipoUsuario> findAllByOrderByIdTipoUsuarioAsc();
    
}
