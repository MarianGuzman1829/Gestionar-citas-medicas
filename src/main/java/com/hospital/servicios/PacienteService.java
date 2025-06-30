package com.hospital.servicios;

import com.hospital.dominio.entidades.Paciente;
import java.util.List;

public interface PacienteService {
    List<Paciente> readAll();
    Paciente read(Long id);
    Paciente save(Paciente paciente);
    void delete(Long id);
    Paciente findByCurp(String curp); // Buscar paciente por CURP
    Paciente obtenerPacientePorId(Long idPaciente);  // Nuevo método 
}
