package com.hospital.servicios;

import com.hospital.dominio.entidades.CitaConsulta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CitaConsultaService {
    List<CitaConsulta> obtenerCitasPorDoctorYFecha(Long idDoctor, LocalDate fecha);
    List<CitaConsulta> obtenerCitasPorPacienteYFecha(Long idPaciente, LocalDate fecha);
    Optional<CitaConsulta> obtenerCitaPorId(Long idCita);
    CitaConsulta crearCita(CitaConsulta citaConsulta);
    CitaConsulta actualizarCita(CitaConsulta citaConsulta);
    void eliminarCita(Long idCita);
    List<CitaConsulta> obtenerTodasLasCitas(); // Método para obtener todas las citas
}
