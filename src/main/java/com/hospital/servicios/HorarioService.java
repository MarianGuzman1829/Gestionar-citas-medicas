package com.hospital.servicios;

import com.hospital.dominio.entidades.Horario;
import java.util.List;

public interface HorarioService {
    List<Horario> readAll();
    Horario read(Long id);
    Horario save(Horario horario);
    void delete(Long id);
}