package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.Medicamento;
import com.hospital.dominio.repositorios.MedicamentoRepository;
import com.hospital.servicios.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<Medicamento> readAll() {
        return medicamentoRepository.findAllByOrderByIdMedicamentoAsc();
    }

    @Override
    public Medicamento read(Long id) {
        return medicamentoRepository.findByIdMedicamento(id).orElse(null); // Buscar medicamento por ID
    }

    @Override
    public Medicamento save(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento); // Guardar o actualizar medicamento
    }

    @Override
    public void delete(Long id) {
        medicamentoRepository.deleteById(id); // Eliminar medicamento por ID
    }

    @Override
    public List<Medicamento> searchByName(String fragmento) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(fragmento); // Buscar por fragmento en el nombre
    }
}
