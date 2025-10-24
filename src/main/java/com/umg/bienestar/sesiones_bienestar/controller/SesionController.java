/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;

import com.umg.bienestar.sesiones_bienestar.dto.SesionDTO;
import com.umg.bienestar.sesiones_bienestar.dto.SesionHistorialDTO;
import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import com.umg.bienestar.sesiones_bienestar.entity.Sesion;
import com.umg.bienestar.sesiones_bienestar.service.impl.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author amada
 */

@RestController
@RequestMapping("/api/sesiones")
@Tag(name = "Sesiones", description = "API para gestión de sesiones")
public class SesionController {
    
    @Autowired
    private SesionService sesionService;
    
    @PostMapping("/{citaId}/iniciar")
    @Operation(summary = "Iniciar sesión", description = "UC-W07: Iniciar una sesión para una cita confirmada")
    public ResponseEntity<Sesion> iniciar(@PathVariable Long citaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.iniciar(citaId));
    }
    
    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar sesión", description = "UC-W07: Finalizar una sesión con observaciones")
    public ResponseEntity<Sesion> finalizar(@PathVariable Long id, @RequestBody SesionDTO sesionDTO) {
        return ResponseEntity.ok(sesionService.finalizar(id, sesionDTO.getObservaciones()));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener sesión por ID")
    public ResponseEntity<Sesion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sesionService.obtenerPorId(id));
    }
    
    @GetMapping
    @Operation(summary = "Listar sesiones con filtros", description = "UC-W04: Consultar historial de sesiones")
    public ResponseEntity<List<SesionHistorialDTO>> listarConFiltros(
        @RequestParam(required = false) Long clienteId,
        @RequestParam(required = false) Long servicioId,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<Sesion> sesiones = sesionService.listarConFiltros(clienteId, servicioId, estado, fechaInicio, fechaFin);
        List<SesionHistorialDTO> sesionesDTO = sesiones.stream()
                .map(this::convertirAHistorialDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sesionesDTO);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar sesiones de un cliente", description = "UC-M05: Ver historial de sesiones")
    public ResponseEntity<List<Sesion>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(sesionService.listarPorCliente(clienteId));
    }
    
    @GetMapping("/periodo")
    @Operation(summary = "Listar sesiones por período")
    public ResponseEntity<List<Sesion>> listarPorPeriodo(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(sesionService.listarPorPeriodo(inicio, fin));
    }
    
    // ========================================
    // MÉTODO AUXILIAR PARA CONVERTIR A DTO
    // ========================================
    private SesionHistorialDTO convertirAHistorialDTO(Sesion sesion) {
        if (sesion == null) {
            return null;
        }
        
        SesionHistorialDTO dto = new SesionHistorialDTO();
        
        // Datos de la sesión
        dto.setId(sesion.getId());
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setFechaFin(sesion.getFechaFin());
        dto.setAsistio(sesion.getAsistio());
        dto.setObservaciones(sesion.getObservaciones());
        
        // Datos de la cita
        if (sesion.getCita() != null) {
            Cita cita = sesion.getCita();
            dto.setCitaId(cita.getId());
            dto.setCitaFechaHora(cita.getFechaHora());
            dto.setCitaEstado(cita.getEstado().toString());
            
            // Datos del cliente
            if (cita.getCliente() != null) {
                dto.setClienteId(cita.getCliente().getId());
                dto.setClienteNombre(cita.getCliente().getNombreCompleto());
                dto.setClienteEmail(cita.getCliente().getEmail());
                dto.setClienteTelefono(cita.getCliente().getTelefono());
            }
            
            // Datos del servicio
            if (cita.getServicio() != null) {
                dto.setServicioId(cita.getServicio().getId());
                dto.setServicioNombre(cita.getServicio().getNombre());
                dto.setServicioPrecio(cita.getServicio().getPrecio());
                dto.setServicioDuracion(cita.getServicio().getDuracionMinutos());
            }
        }
        
        return dto;
    }
}