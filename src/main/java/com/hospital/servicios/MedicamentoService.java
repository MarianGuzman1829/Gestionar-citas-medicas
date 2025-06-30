package com.hospital.servicios;

import com.hospital.dominio.entidades.Medicamento;
import java.util.List;

public interface MedicamentoService {
    List<Medicamento> readAll(); // Obtener todos los medicamentos
    Medicamento read(Long id); // Obtener medicamento por id
    Medicamento save(Medicamento medicamento); // Guardar o actualizar medicamento
    void delete(Long id); // Eliminar medicamento por id
    List<Medicamento> searchByName(String fragmento); // Buscar por nombre
}
