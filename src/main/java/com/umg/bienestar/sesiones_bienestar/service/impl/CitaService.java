/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.*;
import com.umg.bienestar.sesiones_bienestar.exception.BusinessException;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import com.umg.bienestar.sesiones_bienestar.pattern.singleton.ConfiguracionSingleton;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.CitaRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ServicioRepository;
import com.umg.bienestar.sesiones_bienestar.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author amada
 */

@Service
@Transactional
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    private final ConfiguracionSingleton config = ConfiguracionSingleton.getInstance();

    public Cita agendarCita(Long clienteId, Long servicioId, LocalDateTime fechaHora, String notas) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        
        Servicio servicio = servicioRepository.findById(servicioId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        validarCita(clienteId, servicioId, fechaHora, servicio);
        
        Cita cita = new Cita(cliente, servicio, fechaHora, notas);
        cita.setEstado(EstadoCita.PENDIENTE);
        
        Cita guardada = citaRepository.save(cita);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "CREAR",
            "CITA",
            guardada.getId(),
            "Cita agendada para cliente " + cliente.getNombreCompleto() + " - Servicio: " + servicio.getNombre()
        );
        
        return guardada;
    }

    public Cita confirmar(Long citaId) {
        Cita cita = obtenerPorId(citaId);
        
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new BusinessException("Solo se pueden confirmar citas en estado PENDIENTE");
        }
        
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setFechaConfirmacion(LocalDateTime.now());
        
        Cita confirmada = citaRepository.save(cita);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "CONFIRMAR",
            "CITA",
            confirmada.getId(),
            "Cita confirmada para cliente " + confirmada.getCliente().getNombreCompleto()
        );
        
        return confirmada;
    }

    public Cita rechazar(Long citaId, String motivo) {
        Cita cita = obtenerPorId(citaId);
        
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new BusinessException("Solo se pueden rechazar citas en estado PENDIENTE");
        }
        
        cita.setEstado(EstadoCita.RECHAZADA);
        cita.setMotivoRechazo(motivo);
        
        Cita rechazada = citaRepository.save(cita);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "RECHAZAR",
            "CITA",
            rechazada.getId(),
            "Cita rechazada - Motivo: " + motivo
        );
        
        return rechazada;
    }

    public Cita cancelar(Long citaId) {
        Cita cita = obtenerPorId(citaId);
        
        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new BusinessException("No se puede cancelar una cita ya cancelada o atendida");
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = cita.getFechaHora().minusHours(config.getHorasLimiteCancelacion());
        
        if (ahora.isAfter(limite)) {
            throw new BusinessException("No se puede cancelar con menos de " + config.getHorasLimiteCancelacion() + " horas de anticipación");
        }
        
        cita.setEstado(EstadoCita.CANCELADA);
        cita.setFechaCancelacion(ahora);
        
        Cita cancelada = citaRepository.save(cita); 
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "CANCELAR",
            "CITA",
            cancelada.getId(),
            "Cita cancelada para cliente " + cancelada.getCliente().getNombreCompleto()
        );
        
        return cancelada; 
    }

    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }

    public List<Cita> listarPorCliente(Long clienteId) {
        return citaRepository.findByClienteId(clienteId);
    }

    public List<Cita> listarPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    public List<Cita> obtenerHistorialCliente(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findHistorialCliente(clienteId, inicio, fin);
    }

    private void validarCita(Long clienteId, Long servicioId, LocalDateTime fechaHora, Servicio servicio) {
        if (fechaHora.isBefore(LocalDateTime.now().plusHours(config.getHorasMinAnticipacion()))) {
            throw new ValidationException("No se pueden agendar citas con menos de " + config.getHorasMinAnticipacion() + " horas de anticipación");
        }
        
        Long citasPendientes = citaRepository.contarCitasPendientesCliente(clienteId);
        if (citasPendientes >= config.getMaxCitasPendientesPorCliente()) {
            throw new ValidationException("El cliente ya tiene " + config.getMaxCitasPendientesPorCliente() + " citas pendientes");
        }
        
        LocalDateTime inicio = fechaHora;
        LocalDateTime fin = fechaHora.plusMinutes(servicio.getDuracionMinutos());
        
        Long citasEnFranja = citaRepository.contarCitasEnFranja(servicioId, inicio, fin);
        if (citasEnFranja >= servicio.getMaxConcurrentes()) {
            throw new ValidationException("Cupo completo para este horario");
        }
    }
}