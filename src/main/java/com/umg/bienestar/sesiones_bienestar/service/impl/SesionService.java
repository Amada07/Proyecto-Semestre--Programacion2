/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import com.umg.bienestar.sesiones_bienestar.entity.EstadoCita;
import com.umg.bienestar.sesiones_bienestar.entity.Sesion;
import com.umg.bienestar.sesiones_bienestar.exception.BusinessException;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.CitaRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.SesionRepository;
import com.umg.bienestar.sesiones_bienestar.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author amada
 */

@Service
@Transactional
public class SesionService {
    
    @Autowired
    private SesionRepository sesionRepository;
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    public Sesion iniciar(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        
        if (cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new BusinessException("Solo se pueden iniciar sesiones de citas confirmadas");
        }
        
        if (sesionRepository.findByCitaId(citaId).isPresent()) {
            throw new BusinessException("Esta cita ya tiene una sesión iniciada");
        }
        
        Sesion sesion = new Sesion(cita, LocalDateTime.now());
        Sesion guardada = sesionRepository.save(sesion);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "INICIAR",
            "SESION",
            guardada.getId(),
            "Sesión iniciada para cliente " + cita.getCliente().getNombreCompleto()
        );
        
        return guardada;
    }
    
    public Sesion finalizar(Long sesionId, String observaciones) {
        Sesion sesion = obtenerPorId(sesionId);
        
        if (sesion.getFechaFin() != null) {
            throw new BusinessException("Esta sesión ya fue finalizada");
        }
        
        sesion.setFechaFin(LocalDateTime.now());
        sesion.setObservaciones(observaciones);
        sesion = sesionRepository.save(sesion);
        
        Cita cita = sesion.getCita();
        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(cita);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "FINALIZAR",
            "SESION",
            sesion.getId(),
            "Sesión finalizada para cliente " + cita.getCliente().getNombreCompleto()
        );
        
        return sesion;
    }
    
    public Sesion obtenerPorId(Long id) {
        return sesionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con ID: " + id));
    }
    
    public List<Sesion> listarPorCliente(Long clienteId) {
        return sesionRepository.findByClienteId(clienteId);
    }
    
    public List<Sesion> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return sesionRepository.findByFechaBetween(inicio, fin);
    }
    
    // ========================================
    // NUEVO MÉTODO PARA FILTROS
    // ========================================
    public List<Sesion> listarConFiltros(Long clienteId, Long servicioId, String estado, LocalDate fechaInicio, LocalDate fechaFin) {
        // Obtener todas las sesiones
        List<Sesion> sesiones = sesionRepository.findAll();
        
        // Aplicar filtros manualmente
        return sesiones.stream()
            .filter(sesion -> {
                // Filtro por cliente
                if (clienteId != null && !sesion.getCita().getCliente().getId().equals(clienteId)) {
                    return false;
                }
                
                // Filtro por servicio
                if (servicioId != null && !sesion.getCita().getServicio().getId().equals(servicioId)) {
                    return false;
                }
                
                // Filtro por estado (de la cita asociada)
                if (estado != null && !estado.isEmpty()) {
                    try {
                        EstadoCita estadoCita = EstadoCita.valueOf(estado.toUpperCase());
                        if (!sesion.getCita().getEstado().equals(estadoCita)) {
                            return false;
                        }
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                }
                
                // Filtro por fecha inicio
                if (fechaInicio != null) {
                    LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
                    if (sesion.getFechaInicio().isBefore(inicioDateTime)) {
                        return false;
                    }
                }
                
                // Filtro por fecha fin
                if (fechaFin != null) {
                    LocalDateTime finDateTime = fechaFin.atTime(LocalTime.MAX);
                    if (sesion.getFechaInicio().isAfter(finDateTime)) {
                        return false;
                    }
                }
                
                return true;
            })
            .collect(Collectors.toList());
    }
}