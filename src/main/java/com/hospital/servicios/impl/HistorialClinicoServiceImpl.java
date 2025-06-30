package com.hospital.servicios.impl;

import com.hospital.dominio.entidades.HistorialClinico;
import com.hospital.dominio.repositorios.HistorialClinicoRepository;
import com.hospital.servicios.HistorialClinicoService;
import com.hospital.servicios.StoredFile;
import com.hospital.servicios.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialClinicoServiceImpl implements HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    @Transactional(readOnly = true) 
    public List<HistorialClinico> obtenerHistorialesPorPaciente(Long idPaciente) {
        return historialClinicoRepository.findByPaciente_IdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true) 
    public List<HistorialClinico> obtenerTodosLosHistoriales() {
        return historialClinicoRepository.findAllByOrderByIdHistorialAsc();
    }

    @Override
    @Transactional(readOnly = true) 
    public Optional<HistorialClinico> obtenerHistorialPorId(Long idHistorial) {
        return historialClinicoRepository.findByIdHistorial(idHistorial);
    }

    @Override
    @Transactional // Asegura que la creación sea transaccional
    public HistorialClinico crearHistorialClinico(HistorialClinico historialClinico) {
        return historialClinicoRepository.save(historialClinico);
    }

    @Override
    @Transactional
    public HistorialClinico actualizarHistorialClinico(HistorialClinico historialClinico) {
        // Verificar si el historial clínico existe antes de actualizarlo
        if (historialClinico.getIdHistorial() != null && historialClinicoRepository.existsById(historialClinico.getIdHistorial())) {
            return historialClinicoRepository.save(historialClinico);
        } else {
            throw new RuntimeException("Historial clínico no encontrado");
        }
    }

    @Override
    @Transactional
    public void eliminarHistorialClinico(Long idHistorial) {
        historialClinicoRepository.deleteById(idHistorial);
    }

    @Override
    public void guardarYAsociarArchivo(Long idHistorial, MultipartFile file) {
        
        transactionTemplate.execute(status -> {
            try {
                // 1. Buscar la entidad DENTRO de la transacción
                HistorialClinico historial = historialClinicoRepository.findById(idHistorial)
                        .orElseThrow(() -> new RuntimeException("Historial clínico no encontrado con ID: " + idHistorial));

                // 2. Usar el FileStorageService para guardar el archivo y obtener sus datos
                StoredFile storedFile = fileStorageService.storeFile(file);

                // 3. Actualizar la entidad con la información del archivo
                historial.setArchivoAdjuntoPath(storedFile.getFileName());
                historial.setArchivoAdjuntoData(storedFile.getData());

                // 4. Guardar la entidad actualizada.
                historialClinicoRepository.save(historial);
                
            } catch (Exception e) {
                // Si algo sale mal, marcamos la transacción para que se revierta (rollback)
                status.setRollbackOnly();
                throw new RuntimeException("Falló la operación de guardado de archivo: " + e.getMessage(), e);
            }
            return null; // El execute requiere que se devuelva algo.
        });
    }   
}
