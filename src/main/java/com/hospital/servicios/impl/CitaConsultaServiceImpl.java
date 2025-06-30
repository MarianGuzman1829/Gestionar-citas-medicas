package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.CitaConsulta;
import com.hospital.dominio.entidades.Especialidad;
import com.hospital.dominio.repositorios.CitaConsultaRepository;
import com.hospital.servicios.CitaConsultaService;
import com.hospital.servicios.EspecialidadService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaConsultaServiceImpl implements CitaConsultaService {

    @Autowired
    private CitaConsultaRepository citaConsultaRepository;

    @Autowired
    private EspecialidadService especialidadService;

    @Override
    public List<CitaConsulta> obtenerCitasPorDoctorYFecha(Long idDoctor, LocalDate fecha) {
        return citaConsultaRepository.findByDoctor_IdDoctorAndFecha(idDoctor, fecha);
    }

    @Override
    public List<CitaConsulta> obtenerCitasPorPacienteYFecha(Long idPaciente, LocalDate fecha) {
        return citaConsultaRepository.findByPaciente_IdPacienteAndFecha(idPaciente, fecha);
    }

    @Override
    public Optional<CitaConsulta> obtenerCitaPorId(Long idCita) {
        Optional<CitaConsulta> cita = citaConsultaRepository.findById(idCita);
        cita.ifPresent(c -> {
            Hibernate.initialize(c.getDoctor());
            Hibernate.initialize(c.getPaciente());
            Hibernate.initialize(c.getEspecialidad());
            Hibernate.initialize(c.getEstatus());
            Hibernate.initialize(c.getConsultorio());
        });
        return cita;
    }

    @Override
    public CitaConsulta crearCita(CitaConsulta citaConsulta) {
        if (citaConsulta.getEspecialidad() != null && citaConsulta.getEspecialidad().getIdEspecialidad() != null) {
            Especialidad especialidad = especialidadService.obtenerEspecialidadPorId(citaConsulta.getEspecialidad().getIdEspecialidad());
            if (especialidad != null) {
                citaConsulta.setEspecialidad(especialidad);
            } else {
                throw new RuntimeException("Especialidad no encontrada");
            }
        } else {
            throw new RuntimeException("Especialidad es obligatoria");
        }

        return citaConsultaRepository.save(citaConsulta);
    }

    @Override
    public CitaConsulta actualizarCita(CitaConsulta citaConsulta) {
        return citaConsultaRepository.save(citaConsulta);
    }

    @Override
    public void eliminarCita(Long idCita) {
        citaConsultaRepository.deleteById(idCita);
    }

    @Override
    public List<CitaConsulta> obtenerTodasLasCitas() {
        return citaConsultaRepository.findAllByOrderByIdCitaAsc();
    }
}
