package com.hospital.dominio.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Usuario", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED) // <-- ¡LÍNEA AÑADIDA! Esencial para la herencia.
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUsuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
        name = "usuario_id_seq",
        sequenceName = "usuario_idusuario_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "usuario_id_seq"
    )
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[\\w.+\\-]+@(gmail\\.com|outlook\\.com|hotmail\\.com|live\\.com)$",
         message = "Solo se permiten dominios: gmail.com, outlook.com, hotmail.com, live.com") 
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(name = "apellido_pat", nullable = false, length = 50)
    private String apellidoPat;

    @Column(name = "apellido_mat", length = 50)
    private String apellidoMat;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener 10 dígitos")
    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoUsuario", referencedColumnName = "idTipoUsuario", nullable = false)
    private TipoUsuario tipoUsuario;
}
