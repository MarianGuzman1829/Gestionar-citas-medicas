package com.hospital.servicios;

import com.hospital.dominio.entidades.CitaConsulta;
import com.hospital.dominio.entidades.Doctor;

public interface EmailService {
    /**
     * Envía una notificación por correo al paciente y al doctor cuando se crea una nueva cita.
     * @param cita La entidad CitaConsulta recién creada.
     */
    public void enviarNotificacionNuevaCita(CitaConsulta cita);

    /**
     * Envía una notificación por correo al paciente y al doctor cuando una cita se cancela.
     * @param cita La entidad CitaConsulta que fue cancelada.
     * @param motivo El motivo de la cancelación.
     */
    public void enviarNotificacionCancelacion(CitaConsulta cita, String motivo);

    /**
     * Envía un correo de bienvenida a un nuevo usuario.
     * @param destinatario La dirección de correo del nuevo usuario.
     * @param nombre El nombre del nuevo usuario.
     */
    public void enviarNotificacionBienvenida(String destinatario, String nombre);

    void enviarBienvenidaDoctor(Doctor doctor);

    /**
     * Envía un correo con un archivo PDF adjunto.
     * @param destinatario El email del receptor.
     * @param asunto El asunto del correo.
     * @param texto El cuerpo del correo (puede ser HTML).
     * @param pdfBytes El contenido del PDF como un array de bytes.
     * @param nombreArchivoAdjunto El nombre que tendrá el archivo adjunto (ej. "historial.pdf").
     */
    void enviarEmailConAdjunto(String destinatario, String asunto, String texto, byte[] pdfBytes, String nombreArchivoAdjunto);
}