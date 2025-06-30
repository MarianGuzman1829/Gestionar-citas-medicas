package com.hospital.dominio.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Tipo_Usuario", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoUsuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "tipo_usuario_id_seq", sequenceName = "tipo_usuario_idtipousuario_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_usuario_id_seq")
    @Basic(optional = false)
    @Column(name = "idTipoUsuario")
    private Long idTipoUsuario;

    @NotBlank(message = "El nombre del tipo de usuario no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "tipoUsuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //
    private List<Usuario> usuarios;
}